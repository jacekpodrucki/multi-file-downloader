
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiFileDownloader
{
    public static void downloadFile(String fileUrl, String saveFilePath)
    {
        try
        {
            URL url = new URL(fileUrl);
            try (ReadableByteChannel inChannel = Channels.newChannel(url.openStream());
                 WritableByteChannel outChannel = java.nio.file.Files.newByteChannel(Path.of(saveFilePath),
                         StandardOpenOption.CREATE, StandardOpenOption.WRITE))
            {
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                while (inChannel.read(buffer) != -1)
                {
                    buffer.flip();
                    outChannel.write(buffer);
                    buffer.clear();
                }
                System.out.println("Plik zapisany: " + saveFilePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // ścieżki URL
        String[] fileUrls = {
        };

        // ścieżki zapisu
        String[] saveFilePaths = {
        };


        if (fileUrls.length != saveFilePaths.length)
        {
            System.out.println("Liczba ścieżek URL i zapisu musi być taka sama.");
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(fileUrls.length);

        for (int i = 0; i < fileUrls.length; ++i)
        {
            final int index = i;
            executorService.submit(() -> downloadFile(fileUrls[index], saveFilePaths[index]));
        }

        executorService.shutdown();
    }
}
