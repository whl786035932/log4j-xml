/**
 * Constant.java
 * cn.videoworks.edge.constant
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2017年6月19日 		meishen
 *
 * Copyright (c) 2017, TNT All Rights Reserved.
*/

package cn.videoworks.cms.constant;

import java.io.File;

/**
 * ClassName:Constant
 *
 * @author   meishen
 * @version  
 * @since    Ver 1.0.0
 * @Date	 2017年6月19日		下午1:42:43
 *
 */
public class Constant {

	public static final String ARTIST_PATH = File.separator+"static"+File.separator+"artist"+File.separator;

	public static final String RANKING_PATH = File.separator+"static"+File.separator+"ranking"+File.separator;

	public static final String PLAYLIST_PATH = File.separator+"static"+File.separator+"playlist"+File.separator;

	public static final String USER_PATH = File.separator+"static"+File.separator+"user"+File.separator;

	public static final String GIFT_PATH = File.separator+"static"+File.separator+"gift"+File.separator;
	public static final String UGC_PATH = File.separator+"static"+File.separator+"ugc"+File.separator;
	
	public static final String ARTIST ="artist";
	public static final String PLAYLIST ="playlist";
	public static final String RANKING ="ranking";
	public static final String USER ="user";
	public static final String GIFT ="gift";
	public static final String UGC ="ugc";
	
	
	//图片字段名称
	public static final String POSTER ="poster";
	public static final String COVER ="cover";
	public static final String PHOTO ="photo";
	
	
	//baiduBos objectkey
	public static final String ARTIST_PHOTO_OBJECTKEY="image/artist/photo/";
	public static final String PLAYLIST_POSTER_OBJECTKEY="image/playlist/poster/";
	public static final String PLAYLIST_COVER_OBJECTKEY="image/playlist/cover/";
	public static final String RANKING_POSTER_OBJECTKEY="image/ranking/poster/";
	public static final String RANKING_COVER_OBJECTKEY="image/ranking/cover/";
	public static final String USER_PHOTO_OBJECTKEY="image/user/photo/";
	public static final String GIFT_IMG_OBJECTKEY="image/gift/img/";
	public static final String UGC_IMG_OBJECTKEY="image/ugc/img/";
	
	
	//防止出现魔法值，FavorDao类的常量
	public static final String USER_ID="user_id";
	public static final String TITLE="title";
	public static final String STATUS="status";
	public static final String PUBBEGINTIME="pubBeginTime";
	public static final String PUBENDTIME="pubEndTime";
}

