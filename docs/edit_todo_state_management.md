## EditTodoViewModel – State Management Changes

This ViewModel has been refactored to use an **event-based state management approach** to make state updates predictable and easier to maintain.

The UI layer (Composable) remains unchanged and continues to observe `uiState`.

---

## Key Changes Introduced

### 1. Event-Based Updates
All user interactions are now routed through a single `onEvent()` function using sealed classes.

**Why:**
- Centralizes state updates
- Avoids scattered update methods
- Makes user actions explicit and traceable

**Edit Screen Event Class:**
```kotlin
sealed class EditTodoEvent {
    data class OnTitleChanged(val title: String) : EditTodoEvent()
    data class OnDescriptionChanged(val description: String) : EditTodoEvent()
    data class OnStartDateChanged(val date: String) : EditTodoEvent()
    data class OnEndDateChanged(val date: String) : EditTodoEvent()
    data class OnCategoryChanged(val category: TaskGroupCategory) : EditTodoEvent()
    data class LoadTodo(val todoId:Long): EditTodoEvent()
    data class OnStatusChanged(val status: TaskStatus) : EditTodoEvent()
    data class OnPriorityChanged(val priority: TaskPriority) : EditTodoEvent()
    object OnUiReset: EditTodoEvent()
    object OnSaveTodo : EditTodoEvent()
    object OnDeleteTodo : EditTodoEvent()
}
```

**Add Project Screen Event Class:**
```kotlin
sealed class AddProjectEvent{
    data class OnTitleChanged(val title: String) : AddProjectEvent()
    data class OnDescriptionChanged(val description:String): AddProjectEvent()
    data class OnStartDateChanged(val startDate: String) : AddProjectEvent()
    data class OnEndDateChanged(val endDate: String) : AddProjectEvent()
    data class OnCategoryChanged(val category: TaskGroupCategory) : AddProjectEvent()
    data class OnPriorityChanged(val priority: TaskPriority) : AddProjectEvent()
    object OnSaveProject : AddProjectEvent()
    object OnUiReset: AddProjectEvent()

}
```
**Screen Level State Handling:**
```kotlin
sealed class EditTodoState {
    object Idle : EditTodoState()
    object Loading : EditTodoState()
    data class Success(val data: EditTodoUiState) : EditTodoState()
    data class Error(val message: String) : EditTodoState()
}
```

**Method usage**
```kotlin
viewModel.onEvent(EditTodoEvent.OnTitleChanged("New Title"))
viewModel.onEvent(EditTodoEvent.OnDescriptionChanged("Details"))

```