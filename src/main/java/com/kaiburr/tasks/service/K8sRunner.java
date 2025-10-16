package com.kaiburr.tasks.service;

import com.kaiburr.tasks.model.TaskExecution;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class K8sRunner {

    public TaskExecution runInPod(String command) throws Exception {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            String ns = client.getNamespace();
            if (ns == null) ns = "default";

            String name = "exec-" + System.currentTimeMillis();
            Pod pod = new PodBuilder()
                    .withNewMetadata().withName(name).endMetadata()
                    .withNewSpec()
                    .withRestartPolicy("Never")
                    .withContainers(new ContainerBuilder()
                            .withName("runner")
                            .withImage("busybox:1.36")
                            .withCommand("/bin/sh", "-c", command)
                            .build())
                    .endSpec()
                    .build();

            Instant start = Instant.now();
            client.pods().inNamespace(ns).resource(pod).create();

            client.pods().inNamespace(ns).withName(name)
                    .waitUntilCondition(p -> p != null && p.getStatus() != null &&
                                    ("Succeeded".equalsIgnoreCase(p.getStatus().getPhase()) ||
                                            "Failed".equalsIgnoreCase(p.getStatus().getPhase())),
                            60 * 1000L);

            String logs = client.pods().inNamespace(ns)
                    .withName(name)
                    .inContainer("runner")
                    .getLog();

            client.pods().inNamespace(ns).withName(name).delete();

            return new TaskExecution(start, Instant.now(), logs == null ? "" : logs.trim());
        }
    }
}
