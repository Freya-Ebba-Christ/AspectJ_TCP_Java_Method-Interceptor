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
 * Example: java -cp "your-classpath-here" AnnotationAdder MyJavaFile.java MyAnnotation "param1='value1', param2='value2'"
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

public class AnnotationAdder {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: AnnotationAdder <sourceFile> <annotationName> <annotationParameters>");
            return;
        }

        String sourceFile = args[0];
        String annotationName = args[1];
        String annotationParameters = args[2]; // Assume this is a comma-separated list of parameters

        try {
            CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(sourceFile));

            cu.accept(new MethodVisitor(annotationName, annotationParameters), null);

            try ( // Write the modified compilation unit back to the file
                    FileOutputStream out = new FileOutputStream(sourceFile)) {
                out.write(cu.toString().getBytes());
            }

            System.out.println("Annotation added successfully.");
        } catch (IOException e) {
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {

        private final String annotationName;
        private final String annotationParameters;

        public MethodVisitor(String annotationName, String annotationParameters) {
            this.annotationName = annotationName;
            this.annotationParameters = annotationParameters;
        }

        @Override
        public void visit(MethodDeclaration n, Void arg) {
            super.visit(n, arg);

            // Construct the annotation to add
            String annotation = String.format("@%s(%s)", annotationName, annotationParameters);

            // Add the annotation to the method
            n.addAnnotation(annotation);
        }
    }
}
