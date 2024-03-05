/*
 * Application.java
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 * Example: java AnnotationRemover MyJavaFile.java com.example.MyAnnotation
 *
 * @author Freya Ebba Christ 
 */
package aspectj_tcp_java_method_interceptor;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AnnotationRemover {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: AnnotationRemover <sourceFile> <annotationName>");
            System.exit(1);
        }

        String sourceFile = args[0];
        String annotationName = args[1]; // Expect fully qualified name (e.g., "com.example.MyAnnotation")

        try {
            CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(sourceFile));

            cu.accept(new MethodVisitor(annotationName), null);

            // Write the modified compilation unit back to the file
            try (FileOutputStream out = new FileOutputStream(sourceFile)) {
                out.write(cu.toString().getBytes());
            }

            System.out.println("Annotation removed successfully.");
        } catch (IOException e) {
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        private final String annotationName;

        public MethodVisitor(String annotationName) {
            // Normalize annotation name to ensure it matches regardless of import style
            this.annotationName = annotationName.startsWith("@") ? annotationName : "@" + annotationName;
        }

        @Override
        public void visit(MethodDeclaration n, Void arg) {
            super.visit(n, arg);
            // Remove the specified annotation if it exists
            n.getAnnotations().removeIf(a -> a.getName().asString().equals(annotationName) || 
                                             a.getName().getIdentifier().equals(annotationName.replace("@", "")));
        }
    }
}

