package com.example.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TodoEntity::class],
    version = 1,
)
abstract class TodoDatabase : RoomDatabase() {

    abstract val dao: TodoDao
}

/**
 * Criamos um padrão Singleton para a classe `TodoDatabase` para evitar a criação de múltiplas instâncias do banco de dados.
 *
 * - Ao chamar o métódo `provide` pela primeira vez, ele cria e inicializa uma instância do banco de dados.
 * - As próximas chamadas retornam a instância já criada.
 * - O uso de `synchronized` garante que a criação da instância seja segura em ambientes com múltiplas threads, evitando condições de corrida.
 * - A anotação `@Volatile` assegura que a variável `INSTANCE` seja sempre visível e consistente entre as threads.
 */

object TodoDatabaseProvider {

    @Volatile
    private var INSTANCE: TodoDatabase? = null

    fun provide(context: Context): TodoDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todo_app"

            ).build()
            INSTANCE = instance
            instance
        }
    }
}