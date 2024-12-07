package com.example.todolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    /**
     * Insere uma entidade na tabela `todos`.
     *
     * - O comportamento definido é `REPLACE`, ou seja, se uma entidade com o mesmo ID já existir,
     *   ela será substituída pela nova.
     *
     * @param entity A entidade a ser inserida ou substituída na tabela.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TodoEntity)

    /**
     * Remove uma entidade específica da tabela `todos`.
     *
     * - O Room identifica a entidade a ser removida pelo ID informado.
     *
     * @param entity A entidade a ser deletada da tabela.
     */
    @Delete
    suspend fun delete(entity: TodoEntity)

    /**
     * Retorna todas as entidades da tabela `todos` como um fluxo reativo.
     *
     * - O uso de `Flow` garante que qualquer alteração na tabela será refletida automaticamente
     *   na lista retornada.
     * - Não é necessário usar `suspend` porque `Flow` já gerencia operações assíncronas.
     *
     * @return Um fluxo contendo uma lista atualizada de todas as entidades.
     */
    @Query("SELECT * FROM todos")
    fun getAll(): Flow<List<TodoEntity>>

    /**
     * Busca uma entidade específica pelo ID na tabela `todos`.
     *
     * - Retorna `null` caso nenhuma entidade com o ID informado seja encontrada.
     *
     * @param id O identificador da entidade a ser buscada.
     * @return A entidade encontrada ou `null` se não existir.
     */
    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getBy(id: Long): TodoEntity?
}
