package snegrid.move.hangar.business.constant;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public class KmzFileProperties {

    private KmzFileProperties() {

    }

    public static final String WAYLINE_FILE_SUFFIX = ".kmz";

    public static final String FILE_DIR_FIRST = "wpmz";

    public static final String FILE_DIR_SECOND_RES = "res";

    public static final String FILE_DIR_SECOND_TEMPLATE = "template.kml";

    public static final String FILE_DIR_SECOND_WAYLINES = "waylines.wpml";

    public static final String TAG_WPML_PREFIX = "wpml:";

    public static final String TAG_DRONE_INFO = "droneInfo";

    public static final String TAG_DRONE_ENUM_VALUE = "droneEnumValue";

    public static final String TAG_DRONE_SUB_ENUM_VALUE = "droneSubEnumValue";

    public static final String TAG_PAYLOAD_INFO = "payloadInfo";

    public static final String TAG_PAYLOAD_ENUM_VALUE = "payloadEnumValue";

    public static final String TAG_PAYLOAD_SUB_ENUM_VALUE = "payloadSubEnumValue";

    public static final String TAG_TEMPLATE_ID = "templateId";

    public static final String TAG_TEMPLATE_TYPE = "templateType";

    public static final String TAG_IMAGE_FORMAT = "imageFormat";

    /**
     * 飞行距离
     */
    public static final String TAG_DISTANCE = "distance";
    /**
     * 飞行距离
     */
    public static final String TAG_DURATION ="duration";
    /**
     * 飞行速度
     */
    public static final String TAG_AUTO_FLIGHT_SPEED ="autoFlightSpeed";
    /**
     * 飞行高度
     */
    public static final String TAG_TAKEOFF_SECURITY_HEIGHT ="takeOffSecurityHeight";
    /**
     * 航点飞行高度
     */
    public static final String TAG_EXECUTE_HEIGHT ="executeHeight";
    /**
     * Placemark
     */
    public static final String TAG_PLACE_MARK ="Placemark";

    /**
     * Folder
     */
    public static final String TAG_FOLDER ="Folder";

    /**
     * Document
     */
    public static final String TAG_DOCUMENT ="Document";

    /**
     * Point
     */
    public static final String TAG_POINT ="Point";

    /**
     * Point
     */
    public static final String TAG_COORDINATES ="coordinates";


    /**
     * Polygon
     */
    public static final String TAG_POLYGON ="Polygon";

    /**
     * outerBoundaryIs
     */
    public static final  String TAG_OUTER_BOUND_ARY_IS ="outerBoundaryIs";

    /**
     * LinearRing
     */
    public static final  String TAG_LINE_ARR_ING ="LinearRing";


    public static final String  TAG_GLOBAL_TRANSITION_NAL_SPEED = "globalTransitionalSpeed";


    //====================================建图航拍========================================
    /**
     * 航向
     */
    public static final String TAG_OR_THO_CAMERA_OVERLAP_H = "orthoCameraOverlapH";

    /**
     * 旁向
     */
    public static final String TAG_OR_THO_CAMERA_OVER_LAP_W = "orthoCameraOverlapW";

    /**
     * 主航线角度
     */
    public static final String TAG_DIRECTION = "direction";

    /**
     * 边距
     */
    public static final String TAG_MARGIN = "margin";
}

