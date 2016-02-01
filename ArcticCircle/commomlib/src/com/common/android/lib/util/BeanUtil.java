package com.common.android.lib.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

public class BeanUtil {
    private static final String TAG = "BeanUtil";

    public BeanUtil() {
    }
    
    /**
	 * 获取当前时间
	 * 格式  2012-12-21 12:12:12
	 * @return
	 */
	public static String getDateTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss"); 
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

    public static Object getValue(Object obj, String s) {

        Object ret = null;
        Method methods[];
        if (obj.getClass().getClassLoader() != null)
            methods = obj.getClass().getMethods();
        else
            methods = obj.getClass().getDeclaredMethods();

        int i = 0;
        int j = methods.length;
        String functionName = null;
        while (i < j) {
            Method method = methods[i];
            String name = method.getName();
            functionName = "get" + s;
            if (functionName.equalsIgnoreCase(name) || s.equalsIgnoreCase(name)) {
                try {
                    ret = method.invoke(obj, (Object[]) null);
                } catch (Exception e) {
//                    Log.e(TAG,"method.invoke Failed", e);
                    throw new RuntimeException(e);
                }
                break;
            }
            i++;
        }

        return ret;
    }

    // 通过get方法获得属性
    public static Object getValueByMethod(Object obj, String s) {
        Object ret = null;
        String methodName = String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);

        try {
            Method m = obj.getClass().getMethod("get" + methodName);
            ret = m.invoke(obj);

        } catch (NoSuchMethodException e) {
//            Log.w("method.invoke Failed. can't find the method", e);
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
//            Log.w("method.invoke Failed. argument is illegal", e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
//            Log.w("method.invoke Failed. can't access the method", e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
//            Log.w("method.invoke Failed. can't invoke the target", e);
            throw new RuntimeException(e);
        }

        return ret;
    }

    public static void setValue(Object obj, String s) {
        Method methods[];
        if (obj.getClass().getClassLoader() != null)
            methods = obj.getClass().getMethods();
        else
            methods = obj.getClass().getDeclaredMethods();

        int i = 0;
        int j = methods.length;
        String functionName = null;
        while (i < j) {
            Method method = methods[i];
            String name = method.getName();
            Class[] paramters = method.getParameterTypes();
            functionName = "set" + s;
            if (functionName.equalsIgnoreCase(name) || s.equalsIgnoreCase(name)) {
                try {

                    method.invoke(obj, new Object[] { s });
                } catch (Exception e) {
//                    Log.e(TAG,"method.invoke Failed", e);
                    throw new RuntimeException(e);
                }
                break;
            }
            i++;
        }
    }

    public static Object getValueByField(Object obj, String fieldName) {
		Object retObj = null;
		boolean isFound = false;
		String _fieldName = fieldName;
		String _fieldName2 = null;
		int pointIndex = fieldName.indexOf(".");
		try {
			if (pointIndex != -1) {
				_fieldName = fieldName.substring(0, pointIndex);
				_fieldName2 = fieldName.substring(pointIndex + 1);
			}

			Field field = obj.getClass().getDeclaredField(_fieldName);
			field.setAccessible(true); // 抑制Java对修饰符的检查
			isFound = true;

			if (pointIndex != -1) {
				Object _obj = field.get(obj);
				if(_obj != null){
					retObj = getValueByField(_obj, _fieldName2);
				}else{
					retObj = null;
				}
			} else {
				String fieldType = field.getType().getSimpleName();
				if (fieldType.equals("int")) {
					retObj = Integer.valueOf(field.getInt(obj));
				} else if (fieldType.equals("float")) {
					retObj = Float.valueOf(field.getFloat(obj));
				} else if (fieldType.equals("double")) {
					retObj = Double.valueOf(field.getDouble(obj));
				} else if (fieldType.equals("String") || fieldType.equals("Date")) {
					retObj = field.get(obj);
				} else if (fieldType.equals("boolean")) {
					retObj = Boolean.valueOf(field.getBoolean(obj));
				}
			}
		} catch (Exception e) {
			isFound = false;
		}

		// 如果没有找到对应的属性,反射get方法
		if (isFound == false) {
			if (pointIndex != -1) {
				Object _obj = getValueByMethod(obj, _fieldName);
				if(_obj != null){
					retObj = getValueByField(_obj, _fieldName2);
				}else{
					retObj = null;
				}
			} else {
				retObj = getValueByMethod(obj, fieldName);
			}
		}
		return retObj;
	}

    public static void SetValueByField(Object obj, String fieldName, String value) {
        Field[] fields = obj.getClass().getFields();
        String fieldType = null;
        if(value == null || "".equals(value.trim())){
        	return;
        }
        try {
            for (Field field : fields) {
                if (field.getName().equalsIgnoreCase(fieldName)) {
                    fieldType = field.getType().getSimpleName();
                    if (fieldType.equals("int")) {
                        // 原来是field.setInt(obj, Integer.parseInt(fieldName));
                        // yiwen.he修改
                        field.setInt(obj, Integer.parseInt(value));
                    } else if (fieldType.equals("String")) {
                        field.set(obj, value);
                    } else if (fieldType.equals("float")) {
                        field.setFloat(obj, Float.parseFloat(value));
                    } else if (fieldType.equals("double")) {
                        field.setDouble(obj, Double.parseDouble(value));
                    } else if (fieldType.equals("Date")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        field.set(obj, sdf.parse(value));
                    } else if (fieldType.equals("boolean")) {
                        field.setBoolean(obj, Boolean.parseBoolean(value));
                    } else {
                        throw new Exception("不支持的实体类类型(" + fieldType + ")");
                    }
                }

            }
        } catch (Exception e) {
//            Log.e(TAG,"类型转换错误", e);
        }
    }
}
