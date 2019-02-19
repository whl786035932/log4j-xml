/**
 * ShuffleList.java
 * cn.com.cri.dolphin.util
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2018年2月26日 		meishen
 *
 * Copyright (c) 2018, TNT All Rights Reserved.
*/

package cn.videoworks.cms.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * ClassName:ShuffleList
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.1
 * @Date	 2018年2月26日		上午10:29:54
 *
 * @see 	 
 */
public class ShuffleList {
	// 打乱列表实现方法1
    public static <T> void shuffle1(List<T> list) {
        int size = list.size();
        Random random = new Random();
        
        for(int i = 0; i < size; i++) {
            // 获取随机位置
            int randomPos = random.nextInt(size);
            
            // 当前元素与随机元素交换
            T temp = list.get(i);
            list.set(i, list.get(randomPos));
            list.set(randomPos, temp);
        }
    }
    
    // 打乱列表实现方法2
    public static <T> void shuffle2(List<T> list) {
        int size = list.size();
        Random random = new Random();
        
        for(int i = 0; i < size; i++) {
            // 获取随机位置
            int randomPos = random.nextInt(size);
            
            // 当前元素与随机元素交换
            Collections.swap(list, i, randomPos);
        }
    }
    
    // 打乱列表实现方法3
    public static <T> void shuffle3(List<T> list) {
        // 打乱顺序
        Collections.shuffle(list);
    }
    
}

