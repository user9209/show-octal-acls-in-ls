import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class lsOktalACL {

  private static int s = 0;
  private static int si = 4;

  public static void main(String[] args) {

    // args = new String[]{"--selftest"};

    if (args.length == 1) {
      if (args[0].equals("--selftest")) {
        if(selftest()) {
          System.out.println("\nSelftest passed!");
        }
        else
        {
          System.err.println("\nSelftest failed!");
        }
      }
      else if(args[0].equals("--help"))
      {
        System.out.println("example of use 'ls -lA | java lsOktalACL'");
        System.exit(0);
        return;
      }
      else {
        if (args[0].matches(".+\n.+")) {
          StringBuilder sb = new StringBuilder();

          for (String line : args[0].split("\n")) {
            sb.append(transform(line)).append("\n");
          }

          System.out.println(sb.toString());
        } else {
          System.out.println(transform(args[0]));
        }
      }
    } else {
      InputStream in = System.in;
      OutputStream out = System.out;
      try {
        if (in.available() == 0)
          return;

        byte[] buf = new byte[in.available()];

        int size = in.read(buf);

        if (size != buf.length)
          throw new RuntimeException("Input size mismatch!");

        String input = new String(buf);

        for (String line : input.split("\n"))
          out.write((transform(line) + "\n").getBytes(StandardCharsets.UTF_8));

        out.flush();
        out.close();
        in.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static String transform(String inputString) {

    if (inputString.matches(".+\n.+")) {
      StringBuilder sb = new StringBuilder();

      for (String line : inputString.split("\n")) {
        sb.append(transformLine(line)).append("\n");
      }

      return sb.toString().trim();
    }
    return transformLine(inputString);
  }

  private static String transformLine(String inputString) {

    String aclIn = inputString.replaceAll("([dl-])([r-])([w-])([xsS-])([r-])([w-])([xsS-])([r-])([w-])([x-]).+", "$1_$2$3$4_$5$6$7_$8$9$10");

    String acl = parse(aclIn);

    return inputString.replaceAll("([dl-])([r-])([w-])([xsS-])([r-])([w-])([xsS-])([r-])([w-])([x-])", acl);
  }

  private static String parse(String aclIn) {

    String out = "";
    s = 0;
    si = 4;

    String[] acl = aclIn.split("_");

    boolean checkDir = true;
    for (String x : acl) {
      // d -> d, - -> f
      if (checkDir) {
        if (x.equals("-"))
          out += "f ";
        else
          out += x + " ";

        checkDir = false;
        continue;
      }

      // rwx -> 7
      int sum = 0;
      for (char c : x.toCharArray()) {
        sum += getIntValue(c);
      }

      out += sum;
      nextRound();
    }

    if (s > 0)
      out = out.substring(0, 2) + s + out.substring(2);

    return out;
  }

  private static int getIntValue(char c) {
    switch (c) {
      case 'r':
        return 4;
      case 'w':
        return 2;
      case 'x':
        return 1;
      case 's':
        // S +x
        setS();
        return 1;
      case 'S':
        setS();
        return 0;
      default:
        return 0;
    }
  }

  private static void setS() {
    s += si;
  }

  private static void nextRound() {
    si -= 2;
  }


  public static boolean selftest() {

    boolean result = true;

    String[] test = new String[]{
            "-rwxr-xr-x 1 root root 354 Dez 22 18:26 script.sh",
            "lrw-r-xr-x 1 root root 354 Dez 22 18:26 script.sh",
            "drw-r----- 1 root root 354 Dez 22 18:26 script.sh",

            "drwSr-Sr-x 1 root root 354 Dez 22 18:26 script.sh",
            "drwsr----- 1 root root 354 Dez 22 18:26 script.sh",
            "drw-r-S--- 1 root root 354 Dez 22 18:26 script.sh",
            "-rwSr--r-- 1 root root   0 Jan  6 20:14 script.sh",

            "-rwSr--r-- 1 root root   0 Jan  6 20:14 script.sh\n" +
                    "drw-r-S--- 1 root root 354 Dez 22 18:26 script.sh",

    };
    String[] expected = new String[]{
            "f 755 1 root root 354 Dez 22 18:26 script.sh",
            "l 655 1 root root 354 Dez 22 18:26 script.sh",
            "d 640 1 root root 354 Dez 22 18:26 script.sh",

            "d 6645 1 root root 354 Dez 22 18:26 script.sh",
            "d 4740 1 root root 354 Dez 22 18:26 script.sh",
            "d 2640 1 root root 354 Dez 22 18:26 script.sh",
            "f 4644 1 root root   0 Jan  6 20:14 script.sh",

            "f 4644 1 root root   0 Jan  6 20:14 script.sh\n" +
                    "d 2640 1 root root 354 Dez 22 18:26 script.sh",
    };

    for (int i = 0; i < test.length; i++) {
      String output = transform(test[i]);
      System.out.println(output);

      if (output.equals(expected[i])) {
        System.out.println("OK");
      } else {
        System.out.println("Error!");
        result = false;
      }
    }
    return result;
  }
}
