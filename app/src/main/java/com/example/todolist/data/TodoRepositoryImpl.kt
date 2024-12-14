package com.example.todolist.data

import com.example.todolist.domain.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val dao: TodoDao
) : TodoRepository {
    override suspend fun insert(title: String, description: String?, id: Long?) {
        // SE tiver id eu dou um copy pra apenas mudar o title e description
        val entity = id?.let {
            dao.getBy(it)?.copy(
                title = title,
                description = description
            )
            // SENAO eu crio um novo objeto
        } ?: TodoEntity(
            title = title,
            description = description,
            isCompleted = false
        )

        // no dao.insert eu vou ter uma logica pra verificar se o ID ja existe faz o replace com os novos dados
        dao.insert(entity)
    }

    override suspend fun updateCompleted(id: Long, isCompleted: Boolean) {
        // verifico se existe um registro com esse ID no banco de dados, se tiver o codigo continua, se nao tiver ele retorna
        val existingEntity = dao.getBy(id) ?: return
        // usando copy para mudar apenas o parametro 'isCompleted' do meu objeto
        val updatedEntity =
            existingEntity.copy(isCompleted = isCompleted) // <<< nao estamos atualizando o ID, ou seja o insert vai ser usado como UPDATE

        // aqui o insert vai ser usado tambem como UPDATE, isso porque o nosso existingEntity ja tem ID, entao como o dao.insert sabe que tem um registo na tabela
        // com esse iD , quando a gente chama o INSERT ele vai fazer um REPLACE, ou seja ele vai atualizar o registro
        dao.insert(updatedEntity)
    }

    override suspend fun delete(id: Long) {
        val existingEntity = dao.getBy(id) ?: return
        dao.delete(existingEntity)
    }

    override fun getAll(): Flow<List<Todo>> {
        return dao.getAll().map { entities -> entities.toDomain() }
    }

    override suspend fun getBy(id: Long): Todo? {
        return dao.getBy(id)?.toDomain()
    }

    private fun TodoEntity.toDomain(): Todo {
        return Todo(
            id = this.id,
            title = this.title,
            description = this.description,
            isCompleted = this.isCompleted
        )
    }

    private fun List<TodoEntity>.toDomain(): List<Todo> {
        return this.map { it.toDomain() }
    }
}