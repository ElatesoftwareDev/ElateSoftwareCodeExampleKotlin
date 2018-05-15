package es.letmeparkparking.presentation.main

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import es.letmeparkparking.data.models.login.UserInfoToSave
import es.letmeparkparking.presentation.base.BaseMvpView

interface MainView : BaseMvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun logOut()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setLoginState(userInfoToSave: UserInfoToSave?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openWelcomeScreen()

}