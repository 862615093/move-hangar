package snegrid.move.hangar.netty.handler;

import org.springframework.stereotype.Component;
import snegrid.move.hangar.netty.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * 长连接用户管理
 *
 * @author wangwei
 */
@Component
public class NettyUserManager implements NettyHandler {

    //连接用户管理容器
    private Map<String, User> userMap = new ConcurrentHashMap<>();

    public void addUser(User user) {
        userMap.putIfAbsent(user.getId(), user);
    }

    public User getUserById(String id) {
        return userMap.get(id);
    }

    public User getUserByName(String name) {
        for (User user : userMap.values()) {
            if (user.getUserName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUser() {
        return userMap.values().parallelStream().collect(Collectors.toList());
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    //	@Listent(EventType.User_Login_Out)
//	public void removeUser(Event<String, Object> event) {
//		if(event.getSource() == null) {
//			return;
//		}
//		User user = userMap.get(event.getSource());
//		if(user != null){
//			user.destroy();
//			userMap.remove(user.getId());
//		}
//	}
}
