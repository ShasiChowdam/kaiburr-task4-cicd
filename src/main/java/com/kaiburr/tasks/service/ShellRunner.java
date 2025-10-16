package com.kaiburr.tasks.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.stereotype.Service;
import com.kaiburr.tasks.model.TaskExecution;

@Service
public class ShellRunner {
    public TaskExecution run(String command) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        String[] proc = isWindows
                ? new String[]{"cmd.exe", "/c", command}
                : new String[]{"/bin/sh", "-c", command};

        Instant start = Instant.now();
        Process p = new ProcessBuilder(proc).redirectErrorStream(true).start();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
        }
        int code = p.waitFor();
        if (code != 0) sb.append("\n(exit=").append(code).append(")");
        return new TaskExecution(start, Instant.now(), sb.toString().trim());
    }
}
