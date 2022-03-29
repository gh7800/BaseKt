package cn.shineiot.base.binding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @Description 通过反射获取-ViewDataBinding
 * @Author : GF63
 * @Date : 2022/1/13 15:19
 */
public class ViewDataBindingCreator {

    /**
     * Activity使用
     *
     * @param clazz    javaClass
     * @param inflater layoutInflater
     * @return
     */
    public static <Binding extends ViewDataBinding> Binding create(Class<?> clazz, LayoutInflater inflater) {
        return create(clazz, inflater, null);
    }

    public static <Binding extends ViewDataBinding> Binding create(Class<?> clazz, LayoutInflater inflater, ViewGroup root) {
        return create(clazz, inflater, root, false);
    }

    /**
     * Fragment使用
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <Binding extends ViewDataBinding> Binding create(Class<?> clazz, LayoutInflater inflater, ViewGroup root, boolean attachToRoot) {
        Class<?> bindingClass = getBindingClass(clazz);
        Binding binding = null;
        if (bindingClass != null) {
            try {
                Method method = bindingClass.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
                binding = (Binding) method.invoke(null, inflater, root, attachToRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Objects.requireNonNull(binding);
    }

    private static Class<?> getBindingClass(Class<?> clazz) {
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] types = Objects.requireNonNull(parameterizedType).getActualTypeArguments();
        Class<?> bindingClass = null;
        for (Type type : types) {
            if (type instanceof Class<?>) {
                Class<?> temp = (Class<?>) type;
                if (ViewDataBinding.class.isAssignableFrom(temp)) {
                    bindingClass = temp;
                }
            }
        }
        return bindingClass;
    }

}
