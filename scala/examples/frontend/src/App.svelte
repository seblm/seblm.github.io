<script>
    import { onMount } from 'svelte';
    import { router } from './router.js';

    import Header from './Header.svelte';
    import Footer from './Footer.svelte';
    import Item from './Item.svelte';

    import "./app.css";
    import "todomvc-app-css/index.css";
    import "todomvc-common/base.css";

    let currentFilter = "all";
    let items = [];

    async function addItem(event) {
        const response = await fetch(`/api/items`, {method: "POST", body: JSON.stringify({description: event.detail.text,})})
        const newItem = await response.json();
        items.push(newItem);
        items = items;
    }

    async function removeItem(itemId, index) {
        const response = await fetch(`/api/items/${itemId}`, {method: "DELETE"});
        if (response.status === 204) {
            items.splice(index, 1);
            items = items;
        }
    }

    async function toggleAllItems(event) {
        const checked = event.target.checked;
        const action = checked ? "completed" : "uncompleted";
        const response = await fetch(`/api/items/all/${action}`, {method: "PATCH", body: JSON.stringify(items.map((item) => item.id))});
        if (response.status === 204) {
            items = items.map((item) => ({
                ...item,
                completed: checked,
            }));
        }
    }

    async function removeCompletedItems() {
        const completedItemIds = items.filter((item) => item.completed).map((item) => item.id);
        const response = await fetch(`/api/items/all/deleted`, {method: "DELETE", body: JSON.stringify(completedItemIds)});
        if (response.status === 204) {
            items = items.filter((item) => !item.completed);
        }
    }
    
    onMount(async () => {
      router(route => currentFilter = route).init();
      const result = await fetch(`/api/items`);
      items = await result.json();
    });

    $: filtered = currentFilter === "all" ? items : currentFilter === "completed" ? items.filter((item) => item.completed) : items.filter((item) => !item.completed);
    $: numActive = items.filter((item) => !item.completed).length;
    $: numCompleted = items.filter((item) => item.completed).length;
</script>

<Header on:addItem={addItem} />

{#if items.length > 0}
    <main class="main">
        <div class="toggle-all-container">
            <input id="toggle-all" class="toggle-all" type="checkbox" on:change={toggleAllItems} checked={numCompleted === items.length} />
            <label for="toggle-all">Mark all as complete</label>
        </div>
        <ul class="todo-list">
            {#each filtered as item, index (item.id)}
                <Item bind:item on:removeItem={() => removeItem(item.id, index)} />
            {/each}
        </ul>

        <Footer {numActive} {currentFilter} {numCompleted} on:removeCompletedItems={removeCompletedItems} />
    </main>
{/if}
