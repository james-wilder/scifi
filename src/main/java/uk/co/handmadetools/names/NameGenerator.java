package uk.co.handmadetools.names;

import uk.co.handmadetools.hmm.Hmm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NameGenerator {

    private Hmm hmm;

    public NameGenerator() {
        init();
    }

    private void init() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("names.txt");
        List<String> names = new ArrayList<>();
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            reader.lines()
                    .forEach(line -> {
                        String[] split = line.split(",");
                        for (String s : split) {
                            s = s.toLowerCase() + ".";
//                            System.out.println(s);
                            names.add(s);
                        }
                    });
            hmm = Hmm.fromSource(names, 6, 12);
        } else {
            System.out.println("Uh-oh");
        }
    }

    public String newName() {
        return hmm.generateName();
    }


}
