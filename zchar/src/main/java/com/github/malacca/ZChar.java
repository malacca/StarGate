package com.github.malacca;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;
import android.content.ClipData;
import android.content.ClipboardManager;

public class ZChar {
    private static final int SPACE_LOW = 8203;
    private static final int SPACE_HIG = 8204;
    private static final int SPACE_WRD = 8205;
    private static final int SPACE_SPT = 8206;

    // 明文 -> 零宽字符
    public static String encode(String data) {
        StringBuilder encode = new StringBuilder();
        encode.append((char) SPACE_SPT);
        for (int i = 0; i < data.length(); i++ ) {
            int val = data.charAt(i);
            int len = Math.max(1, Integer.SIZE - Integer.numberOfLeadingZeros(val));
            int mask = 1 << (len - 1);
            while(len > 0) {
                encode.append((val & mask) == 0 ? (char) SPACE_LOW : (char) SPACE_HIG);
                mask >>= 1;
                len--;
            }
            encode.append((char) SPACE_WRD);
        }
        encode.setLength(encode.length() - 1);
        encode.append((char) SPACE_SPT);
        return encode.toString();
    }

    // 零宽字符 -> 明文 (只取第一个)
    public static String decode(String data) {
        List<String> all = decodeZChar(data, false);
        return all.size() > 0 ? all.get(0) : null;
    }

    // 零宽字符 -> 明文 (返回所有可能的隐藏字符)
    public static List<String> decodeAll(String data) {
        return decodeZChar(data, true);
    }

    // 零宽字符 -> 明文
    private static List<String> decodeZChar(String data, boolean all) {
        List<String> encodes = new ArrayList<>();
        int len = data == null ? 0 : data.length();
        if (len == 0) {
            return encodes;
        }
        int k = 0, chr;
        boolean find = false, error = false;
        StringBuilder encode = new StringBuilder();
        StringBuilder charset = new StringBuilder();
        while (k < len) {
            chr = data.charAt(k);
            if (chr == SPACE_SPT) {
                if (find) {
                    // 零宽字符结束
                    if (!error) {
                        if (charset.length() > 0) {
                            encode.append((char) Integer.parseInt(charset.toString(), 2));
                        }
                        encodes.add(encode.toString());
                        if (!all) {
                            break;
                        }
                    }
                    find = false;
                } else {
                    // 零宽字符开始
                    find = true;
                    error = false;
                }
                encode.setLength(0);
            } else if (find && !error) {
                switch (chr) {
                    case SPACE_LOW:
                        charset.append("0");
                        break;
                    case SPACE_HIG:
                        charset.append("1");
                        break;
                    case SPACE_WRD:
                        encode.append((char) Integer.parseInt(charset.toString(), 2));
                        charset.setLength(0);
                        break;
                    default:
                        error = true;
                        break;
                }
            }
            k++;
        }
        return encodes;
    }

    // 从 android 剪贴板获取 decode String
    public static String getDecodeFromClipboard(Context context) {
        List<String> all = decodeZChar(getClipboardData(context), false);
        return all.size() > 0 ? all.get(0) : null;
    }

    // 从 android 剪贴板获取所有 decode String
    public static List<String> getAllDecodeFromClipboard(Context context) {
        return decodeZChar(getClipboardData(context), true);
    }

    private static String getClipboardData(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        return clipData == null ? null : clipData.getItemAt(0).getText().toString();
    }
}