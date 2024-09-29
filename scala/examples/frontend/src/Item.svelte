<script>
    import { createEventDispatcher, tick } from 'svelte';

    export let item;

    const dispatch = createEventDispatcher();
    
    let editing = false;

    function removeItem() {
        dispatch('removeItem');
    }

    function startEdit() {
       editing = true;
    }

    function handleEdit(event) {
        if (event.key === "Enter")
            event.target.blur();
        else if (event.key === "Escape") 
            editing = false;
    }

    async function updateDescription(description) {
        const response = await fetch(`/api/items/${item.id}`, {method: "PATCH", body: JSON.stringify({description: description})});
        return await response.json();
    }

    async function updateItem(event) {
        if (!editing) return;
        const { value } = event.target;
        if (value.length) {
            const updatedItem = await updateDescription(value);
            item.description = updatedItem.description;
            editing = false;
        } else {
          removeItem();
          editing = false;
        }
    }

    async function updateCompleted(event) {
        const {checked} = event.target;
        const response = await fetch(`/api/items/${item.id}`, {method: "PATCH", body: JSON.stringify({completed: checked})})
        const updatedItem = await response.json();
        item.completed = updatedItem.completed;
    }

    async function focusInput(element) {
        await tick();
        element.focus();
    }
</script>

<li class:completed={item.completed} class:editing>
    <div class="view">
        <input class="toggle" type="checkbox" on:change={updateCompleted} checked={item.completed}/>
        <!-- svelte-ignore a11y-label-has-associated-control -->
        <label on:dblclick={startEdit}>{item.description}</label>
        <button on:click={removeItem} class="destroy" />
    </div>

    {#if editing}
        <div class="input-container">
            <input value={item.description} id="edit-todo-input" class="edit" on:keydown={handleEdit} on:blur={updateItem} use:focusInput />
            <label class="visually-hidden" for="edit-todo-input">Edit Todo Input</label>
        </div>
    {/if}
</li>
