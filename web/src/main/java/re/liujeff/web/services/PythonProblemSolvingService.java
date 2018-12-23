package re.liujeff.web.services;

import re.liujeff.web.model.Hypothesis;
import re.liujeff.web.model.Proof;
import re.liujeff.web.model.Pythonable;
import re.liujeff.web.model.Theorem;

import java.io.*;
import java.util.List;

public class PythonProblemSolvingService implements ProblemSolvingService {

    private List<Theorem> theorems;

    public PythonProblemSolvingService() {

    }

    @Override
    public void addTheorem(Theorem theorem) {
        theorems.add(theorem);
    }

    @Override
    public Proof getSolution(List<Hypothesis> constraints, List<Hypothesis> goals) {
        StringBuilder sb = new StringBuilder();

        String fs = System.getProperty("file.separator");
        String path = "engine"+fs+"release"+fs+"get_solution.py";

        appendln(theorems, sb);
        appendln(goals, sb);
        appendln(constraints, sb);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream("engine"+fs+"release"+fs+"temp.txt")))) {
            writer.write(sb.toString());
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
