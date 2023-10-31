package snegrid.move.hangar.utils.fly;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.exception.ServiceException;
import snegrid.move.hangar.minio.config.MinioConfig;
import snegrid.move.hangar.utils.spring.SpringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static snegrid.move.hangar.business.constant.KmzFileProperties.*;
import static snegrid.move.hangar.constant.MagicValue.COMMA;

/**
 * kmz文件处理工具类
 *
 * @author wamngwei
 */
@Component
public class KmzUtil {

    private static final Logger logger = LoggerFactory.getLogger(KmzUtil.class);

    private static final MinioConfig minioConfig = SpringUtils.getBean(MinioConfig.class);

    /**
     * 获取kmz执行文件航点信息
     *
     * @param inputStream kmz输入流
     * @return 结果
     */
    public static List<KmzRoutePoint> parseKmzRoutePoint(@NonNull InputStream inputStream) throws Exception {
        ZipInputStream zipInputStream = getCurrentZipInputStream(inputStream, "wpmz/waylines.wpml");
        SAXReader reader = new SAXReader();
        Document document = reader.read(zipInputStream);
        Element rootElement = document.getRootElement().element(TAG_DOCUMENT);
        Element folderElement = rootElement.element(TAG_FOLDER);
        List<?> placemarkElementList = folderElement.elements(TAG_PLACE_MARK);
        List<KmzRoutePoint> list = new LinkedList<>();
        for (int i = 0; i < placemarkElementList.size(); i++) {
            Element element = ((Element) placemarkElementList.get(i)).element(TAG_POINT).element(TAG_COORDINATES);
            String[] split = element.getText().trim().split(COMMA);
            KmzRoutePoint kmzRoutePoint = new KmzRoutePoint(i, Double.valueOf(split[0]), Double.valueOf(split[1]));
            list.add(kmzRoutePoint);
        }
        return list;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KmzRoutePoint {

        /**
         * 点在线路中的序号从0开始
         */
        private Integer pointNumber;

        /**
         * 经度 wgs84 坐标
         */
        private Double longitudeWgs84;

        /**
         * 维度 wgs84坐标
         */
        private Double latitudeWgs84;

        @Override
        public String toString() {
            return "KmzRoutePoint{" +
                    "pointNumber=" + pointNumber +
                    ", longitudeWgs84=" + longitudeWgs84 +
                    ", latitudeWgs84=" + latitudeWgs84 +
                    '}';
        }
    }

    /**
     * 获取当前操作ZipInputStream对象
     */
    private static ZipInputStream getCurrentZipInputStream(InputStream inputStream, String matchFileName) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream, StandardCharsets.UTF_8);
        ZipEntry nextEntry = zipInputStream.getNextEntry();
        while (ObjectUtil.isNotNull(nextEntry)) {
            if (matchFileName.equals(nextEntry.getName())) {
                return zipInputStream;
            }
            nextEntry = zipInputStream.getNextEntry();
        }
        throw new ServiceException("源kmz文件异常");
    }
}
