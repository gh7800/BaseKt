package cn.shineiot.base.mvvm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.reflect.KClass


/**
 * 在Activity中初始化viewModel
 */
@Suppress("UNCHECKED_CAST")
fun <BVM : BaseViewModel> initViewModelS(
    activity: FragmentActivity,
    vmClass: KClass<BVM>,
    rClass: KClass<out BaseRepository>
) =
    ViewModelProvider(activity, object : ViewModelProvider.NewInstanceFactory() {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
            return vmClass.java.getConstructor(rClass.java)
                .newInstance(rClass.java.newInstance()) as VM
        }
    }).get(vmClass.java)


/**
 * 在Fragment中初始化viewModel
 */
@Suppress("UNCHECKED_CAST")
fun <BVM : BaseViewModel> initViewModelS(
    fragment: Fragment,
    vmClass: KClass<BVM>,
    rClass: KClass<BaseRepository>
) =
    ViewModelProvider(fragment, object : ViewModelProvider.NewInstanceFactory() {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
            return vmClass.java.getConstructor(rClass.java)
                .newInstance(rClass.java.newInstance()) as VM
        }
    }).get(vmClass.java)