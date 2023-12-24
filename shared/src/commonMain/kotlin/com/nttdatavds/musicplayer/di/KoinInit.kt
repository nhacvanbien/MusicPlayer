import com.nttdatavds.musicplayer.di.dispatcherModule
import com.nttdatavds.musicplayer.di.ktorModule
import com.nttdatavds.musicplayer.di.platformModule
import com.nttdatavds.musicplayer.di.repositoryModule
import com.nttdatavds.musicplayer.di.useCasesModule
import com.nttdatavds.musicplayer.di.viewModelModule
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

class KoinInit {
    fun init(appDeclaration: KoinAppDeclaration = {}): Koin {
        return startKoin {
            modules(
                listOf(
                    platformModule(),
                    dispatcherModule,
                    ktorModule,
                    repositoryModule,
                    useCasesModule,
                    viewModelModule,
                )
            )
            appDeclaration()
        }.koin
    }
}
