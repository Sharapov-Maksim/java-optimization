jdk 16.0.1:
```
Compiled from "P1.java"
public class nsu.fit.javaperf.lab2.P1 {
  public nsu.fit.javaperf.lab2.P1();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: ldc           #7                  // String
       2: astore_1
       3: iconst_0
       4: istore_2
       5: iload_2
       6: sipush        10000
       9: if_icmpge     29
      12: aload_1
      13: iload_2
      14: invokestatic  #9                  // Method java/lang/String.valueOf:(I)Ljava/lang/String;
      17: invokedynamic #15,  0             // InvokeDynamic #0:makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      22: astore_1
      23: iinc          2, 1
      26: goto          5
      29: getstatic     #19                 // Field java/lang/System.out:Ljava/io/PrintStream;
      32: aload_1
      33: invokevirtual #25                 // Method java/lang/String.length:()I
      36: invokevirtual #29                 // Method java/io/PrintStream.println:(I)V
      39: return
}
```

Похоже, что компилятор соптимизировал конкатенацию строк при помощи вызова `makeConcatWithConstants`. Похоже эту оптимизацию добавили в Java 9: https://www.guardsquare.com/blog/string-concatenation-java-9-untangling-invokedynamic
