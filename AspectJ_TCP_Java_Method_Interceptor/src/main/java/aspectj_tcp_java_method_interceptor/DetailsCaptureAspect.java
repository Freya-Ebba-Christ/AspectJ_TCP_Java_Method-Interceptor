/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package aspectj_tcp_java_method_interceptor;

/*
 * DetailsCaptureAspect.java
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
 * @author Freya Ebba Christ
 */


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

@Aspect
@Component
public class DetailsCaptureAspect {

    // Method to log data over a socket (SSL or regular)
    private void sendDataOverSocket(String data, String serverAddress, int serverPort, boolean useSSL) {
        try {
            SocketFactory factory = useSSL ? SSLSocketFactory.getDefault() : SocketFactory.getDefault();
            try (Socket socket = factory.createSocket(serverAddress, serverPort);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                writer.write(data);
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to send data over " + (useSSL ? "encrypted" : "regular") + " socket: " + e.getMessage());
        }
    }

    // Method to log data to a file
    private void logDataToFile(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("methodDetails.log", true))) {
            writer.write(data);
            writer.newLine();
        } catch (Exception e) {
            System.err.println("Failed to log data to file: " + e.getMessage());
        }
    }

    @Around("@annotation(captureDetails)")
    public Object captureMethodDetails(ProceedingJoinPoint joinPoint, CaptureDetails captureDetails) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        Object returnValue = joinPoint.proceed(); // Proceed with the method call

        String data = String.format("Class Name: %s, Method Name: %s, Parameters: %s, Return Value: %s",
                                    className, methodName, Arrays.toString(args), returnValue);

        if ("socket".equals(captureDetails.outputType())) {
            sendDataOverSocket(data, captureDetails.serverAddress(), captureDetails.serverPort(), captureDetails.useSSL());
        } else if ("file".equals(captureDetails.outputType())) {
            logDataToFile(data);
        }

        return returnValue;
    }
}





