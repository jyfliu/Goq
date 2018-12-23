package re.liujeff.web.services;

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
        String path = ".." + fs + "engine"+fs+"release"+fs+"get_solution.py";

        appendln(theorems, sb);
        appendln(goals, sb);
        appendln(constraints, sb);
        try {
            Path file = Paths.get(".." + fs + "engine" + fs + "release" + fs + "temp.txt");
            Files.write(file, Collections.singleton(sb.toString()), Charset.forName("UTF-8"));
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        StringBuilder result = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("python " + path+" temp.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s = null;
            while ((s = reader.readLine()) != null) {
                result.append(s).append("\n");
            }
        } catch(IOException ioe) {
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
