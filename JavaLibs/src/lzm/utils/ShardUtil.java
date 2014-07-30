package lzm.utils;


public class ShardUtil {
	/**
	 * 对字符串uid进行md5计算后，然后在进行默认hash分布计算
	 * @param uid
	 * @param num
	 * @return
	 */
	public synchronized static int getHashIndexMD5(String uid, int num){
		uid = MD5.encode(uid);
		return getByteHashIndex(uid.getBytes(), num);
	}
	
	/**
	 * hash byte分布计算
	 * @param bKey 
	 * @param num
	 * @return
	 */
	private static int getByteHashIndex(byte[] bKey, int num){
		if(bKey.length<4){
			return (bKey[0]&0xFF)%num;
		}
		long res = ((long)(bKey[3]&0xFF) << 24) | ((long)(bKey[2]&0xFF) << 16) | ((long)(bKey[1]&0xFF) << 8) | (long)(bKey[0]&0xFF);
		return (int)res%num;
	}
}
