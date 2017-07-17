import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by andreas on 15.07.17.
 */
public class Test {

    public static void main(String[] args) throws IOException {

        String fileName = "Test.txt";

        GitHubClient client = new GitHubClient().setCredentials("ubx", "altavista99");
        Gist gist = new Gist().setDescription("Just a test ........").setPublic(true);
        String content = System.currentTimeMillis() + "  Time in ms";
        GistFile file = new GistFile().setContent(content);
        gist.setFiles(Collections.singletonMap(fileName, file));
        gist = new GistService(client).createGist(gist);

        System.out.println(getRawUrl(client.getUser(), gist.getId(), fileName));
        Scanner scanner = new Scanner(new URL(getRawUrl(client.getUser(), gist.getId(), fileName)).openStream());
        String contentRcv = scanner.nextLine();
        int i = contentRcv.compareTo(content);

        System.out.println(getRawUrl(fileName, new URL(gist.getUrl())));
        scanner = new Scanner(new URL(getRawUrl(fileName, new URL(gist.getUrl()))).openStream());
        contentRcv = scanner.nextLine();
        i = contentRcv.compareTo(content);
    }

    private static String getRawUrl(String fileName, URL url) throws IOException {
        Scanner scanner = new Scanner(url.openStream());
        JSONObject jobj = new JSONObject(scanner.nextLine());
        JSONObject files = jobj.getJSONObject("files");
        JSONObject f = files.getJSONObject(fileName);
        return f.getString("raw_url");
    }

    private static String getRawUrl(String user, String id, String fileName) {
        return "https://gist.github.com/" + user + "/" + id + "/raw/" + fileName;
    }


}
