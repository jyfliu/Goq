package re.liujeff.web.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import re.liujeff.web.model.Hypothesis;
import re.liujeff.web.model.Proof;
import re.liujeff.web.model.Pythonable;
import re.liujeff.web.model.Theorem;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Primary
public class PythonProblemSolvingService implements ProblemSolvingService {

    private List<Theorem> theorems;

    public PythonProblemSolvingService() {
        theorems = new ArrayList<>();
    }

    @Override
    public void addTheorem(Theorem theorem) {
        theorems.add(theorem);
    }

    @Override
    public Proof getSolution(List<Hypothesis> goals, List<Hypothesis> constraints) {
        StringBuilder sb = new StringBuilder();

        String fs = System.getProperty("file.separator");
        String path = ".." + fs + "engine"+fs+"release"+fs;

        appendln(theorems, sb);
        appendln(goals, sb);
        appendln(constraints, sb);
        try {
            Path file = Paths.get(path+ "temp.txt");
            Files.write(file, Collections.singleton(sb.toString()), Charset.forName("UTF-8"));
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        StringBuilder result = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("py " + path +"get_solution.py temp.txt");
            process.waitFor();
            Files.lines(Paths.get(path + "proof.txt")).forEach(x->result.append(x).append("\n"));
        } catch(IOException | InterruptedException ioe) {
            ioe.printStackTrace();
        }
        return new Proof(result.toString());
    }

    private void appendln(List<? extends Pythonable> list, StringBuilder sb) {
        sb.append(list.size());
        sb.append("\n");
        for (Pythonable item : list) {
            sb.append(item.toPython("#"));
            sb.append("\n");
        }
    }

}
