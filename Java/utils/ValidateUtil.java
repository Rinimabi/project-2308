package Java.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidateUtil {
    /**
     * 校验hibernate.validate注解，返回错误信息
     **/
    public static <T> List<String> validateAnnotation(T t) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        List<String> messageList = new ArrayList<>();
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            messageList.add(constraintViolation.getMessage());
        }
        return messageList;
    }

    /**
     * 校验类的参数是否为空
     *
     * @param fieldList 字段名称（逗号间隔）
     * @param nameList  字段中文名（逗号间隔）
     * @param object    校验类
     **/
    public static void validFieldNullable(String fieldList, String nameList, Object object) {
        String[] fields = fieldList.split(",");
        String[] names = nameList.split(",");
        if (fields.length == 0) {
            return;
        }
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i];
            Field field = null;
            try {
                field = object.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            Object value = null;
            try {
                assert field != null;
                field.setAccessible(true);
                value = field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (value == null || value.equals("")) {
                throw new BizException(names[i] + "不能为空");
            }
        }
    }
}