package cn.shineiot.basic.binding;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @Description 通过反射获取-ViewBinding
 * @Author : GF63
 * @Date : 2022/1/13 15:19
 */
public class ViewBindingCreator {

    /**
     * Activity使用
     * @param clazz javaClass
     * @param inflater  layoutInflater
     * @return
     */
    public static <Binding extends ViewBinding> Binding create(Class<?> clazz, LayoutInflater inflater) {
        return create(clazz, inflater, null);
    }

    public static <Binding extends ViewBinding> Binding create(Class<?> clazz, LayoutInflater inflater, ViewGroup root) {
        return create(clazz, inflater, root, false);
    }

    /**
     * Fragment使用
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <Binding extends ViewBinding> Binding create(Class<?> clazz, LayoutInflater inflater, ViewGroup root, boolean attachToRoot) {
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
                if (ViewBinding.class.isAssignableFrom(temp)) {
                    bindingClass = temp;
                }
            }
        }
        return bindingClass;
    }

    /**
     * android 9.0以上才可用
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static <VB extends ViewBinding> VB createViewBinding(Class targetClass, LayoutInflater layoutInflater)
    {
        Type type = targetClass.getGenericSuperclass();

        if (type instanceof ParameterizedType)
        {
            try
            {
                Type[] types = ((ParameterizedType) type).getActualTypeArguments();

                for (Type type1 : types)
                {
                    if (type1.getTypeName().endsWith("Binding"))
                    {
                        Method method = ((Class<VB>) type1).getMethod("inflate",
                                LayoutInflater.class);
                        return (VB) method.invoke(null, layoutInflater);
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        } return null;
    }
}
