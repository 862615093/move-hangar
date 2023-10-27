package snegrid.move.hangar.netty.message;

/**
 * 协议消息号
 *
 * @author wangwei
 */
public final class MessageType {
	
	/*************************************************** 用户请求操作类 **************************************************/

	//登录
	public static final int LONGIN = 100;

	//开始任务
	public static final int START_TASK = 2000;

	//无人机控制指令
	public static final int DRONE_CONTROL = 1;

	//机库控制指令
	public static final int HANGAR_CONTROL = 2;


	/** 发送消息到所有 */
	public static final int Message_Send_All = 102;
	/** 发送消息到个别 */
	public static final int Message_Send_One = 103;
	/** 通过房间号加入房间 */
	public static final int Join_Room_ById = 104;
	/** 随机加入房间 */
	public static final int Join_Room_Random = 105;
	/** 创建房间 */
	public static final int Create_Room = 106;
	/** 离开房间 */
	public static final int Leave_Room = 107;


	
	/*************************************************** 返回客户端协议 通知类 ********************************************/

	//成功响应
	public static final int SUCCESS = 200;

	//失败响应
	public static final int ERROR = 500;
	
	/** 服务器主动推送消息 */
	public static final int System_Push_Message = -200;
	/** 有错误信息 返回给客户端用 */
	public static final int Has_Error = -400;
	/** 加入房间通知 */
	public static final int User_Join_Room = -101;
	/** 离开房间通知 */
	public static final int User_Leave_Room = -102;
	
}
