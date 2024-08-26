package com.wdbyte.bing.html;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.wdbyte.bing.LogUtils;

/**
 * @author niulang
 * @date 2022/08/18
 */
public class HtmlFileUtils {

    public static Path BING_HTML_ROOT = Paths.get("docs/");
    private static Path BING_HTML_INDEX_TEMPLATE = Paths.get("docs/bing-template.html");
    private static Path BING_HTML_DETAIL_TEMPLATE = Paths.get("docs/bing-detail.html");

    public static String readIndexTemplateFile() throws IOException {
        byte[] bytes = Files.readAllBytes(BING_HTML_INDEX_TEMPLATE);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String readDetailTemplateFile() throws IOException {
        byte[] bytes = Files.readAllBytes(BING_HTML_DETAIL_TEMPLATE);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeIndexHtml(String html) throws IOException {
        Path path = BING_HTML_ROOT.resolve("index.html");
        write(path, html);
    }

    public static void writeDetailHtml(String html, String urlPath) throws IOException {
        Path path = BING_HTML_ROOT.resolve(urlPath);
        write(path, html);
    }

    private static void write(Path path, String html) throws IOException {
        Path parent = path.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent); // 使用 createDirectories 递归创建目录
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
        Files.write(path, html.getBytes(StandardCharsets.UTF_8));
        LogUtils.log("HTML written to %s", path.toString());
    }

    public static void writeMonthHtml(String month, String html) throws IOException {
        Path path = BING_HTML_ROOT.resolve(month + ".html");
        write(path, html);
    }

    /**
     * 删除指定目录下的所有 HTML 文件
     * @param directoryPath 需要清理的目录路径
     * @throws IOException 如果删除过程中发生错误
     */
    public static void deleteAllHtmlFiles(Path directoryPath) throws IOException {
        if (Files.exists(directoryPath)) {
            try (Stream<Path> paths = Files.walk(directoryPath)) {
                paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".html"))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                                LogUtils.log("Deleted file: %s", path.toString());
                            } catch (IOException e) {
                                LogUtils.log("Failed to delete file: %s", path.toString());
                            }
                        });
            }
        }
    }
}
