package snegrid.move.hangar;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import snegrid.move.hangar.utils.fly.KmzUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@Slf4j
public class KmzDemo {

//    @Autowired


    @Test
    public void t() throws Exception {
        String url = "/usr/local/java/test.kmz";
        FileInputStream fileInputStream = new FileInputStream(new File(url));
        List<KmzUtil.KmzRoutePoint> list = KmzUtil.parseKmzRoutePoint(fileInputStream);
        log.info("list={}", list);
    }


}
