package com.docmala;

import net.sourceforge.plantuml.*;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.preproc.Defines;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)value,
                (byte)(value >>> 8),
                (byte)(value >>> 16),
                (byte)(value >>> 24) };
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        while(true) {
            String source = new String();

            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            final StringBuilder sb = new StringBuilder();
            String s;

            while (true) {
                if( (s = bufferedReader.readLine()) == null )
                    System.exit(0);

                sb.append(s);
                sb.append("\n");

                if (s.contains("@enduml"))
                    break;
            }

            source = sb.toString();

            final SourceStringReader sourceStringReader = new SourceStringReader(new Defines(), source, new ArrayList<>());
            final ByteArrayOutputStream imageData = new ByteArrayOutputStream();
            final String result = sourceStringReader.generateImage(imageData, 0, new FileFormatOption(FileFormat.SVG));

            if ("(error)".equalsIgnoreCase(result)) {
                System.err.println("ERROR");
                final Diagram system = sourceStringReader.getBlocks().get(0).getDiagram();
                final PSystemError sys = (PSystemError) system;
                System.err.println(sys.getHigherErrorPosition());
                for (ErrorUml er : sys.getErrorsUml()) {
                    System.err.println(er.getError());
                }
            }

            System.out.write(intToByteArray(imageData.size()));
            System.out.write(imageData.toByteArray());
        }
    }
}
