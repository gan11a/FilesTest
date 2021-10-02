package cf.iqas;
import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.core.ZipFile;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFileTest {
    @Test
    void uploadFileTest(){
        Selenide.open("https://the-internet.herokuapp.com/upload");
        $("input[type='file']").uploadFromClasspath("example.txt");
        $("#file-submit").click();
        $("#uploaded-files").shouldHave(Condition.text("example.txt"));
    }
    @Test
    void txtFileTest() throws Exception{
        String result;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("example.txt")){
            result = new String (is.readAllBytes(), StandardCharsets.UTF_8);
        }
        assertThat(result).contains("Hello, world");
    }
    @Test
    void parsedPDFTest() throws Exception{
        PDF parsed = new PDF(getClass().getClassLoader().getResourceAsStream("example.pdf"));
        assertThat(parsed.text).contains("This is a test PDF document");
    }
    @Test
    void parseXlsTest() throws Exception{
        XLS parsed = new XLS(getClass().getClassLoader().getResourceAsStream("example.xls"));
        assertThat(parsed.excel.getSheetAt(0).getRow(0).getCell(0).getNumericCellValue()).isEqualTo(10101);
        assertThat(parsed.excel.getSheetAt(0).getRow(0).getCell(1).getNumericCellValue()).isEqualTo(21);
    }
    @Test
    void parseZipFile() {
        String path = "src/test/resources/Hello World.zip";
        String unarchive = "src/test/unarchivefiles";
        String password = "1111";
        try {
            ZipFile zipFile = new ZipFile(path);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(unarchive);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

