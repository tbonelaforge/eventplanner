package eventplanner;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

public class GetSystemStatusEvent implements Runnable {
    @Override
    public void run() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://local.fitbit.com/mgmt/getSystemStatus");
        try {
            CloseableHttpResponse response = httpClient.execute(httpget);
            //System.out.println("Got a CloseableHttpResponse:\n");
            //System.out.println(response);
            //System.out.println("The status line is:\n");
            //System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            //System.out.println("The entity is:\n");
            //System.out.println(entity);
            if (entity != null) {
                System.out.printf("Inside thread %s, got response: %s%n", Thread.currentThread().getName(), response.getStatusLine());
                long len = entity.getContentLength();
                //System.out.printf("The entity.getContentLength() is: %d%n", len);
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                } else {
                    //System.out.println("About to try and stream the entity to System.out...\n");
                    entity.writeTo(System.out);
                    //System.out.println("\nThat was it! that was the content!\n");
                    System.out.println();
                }
            }
            response.close();
        } catch(Exception e) {
            System.out.println("Got an exception trying to run the GetSystemStatus request...\n");
            System.out.println(e);
            System.out.println("With message:\n");
            System.out.println(e.getMessage());
        }
    }
}
