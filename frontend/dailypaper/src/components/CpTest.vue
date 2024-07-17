<template>
    <ul>
        <li v-for="(item, index) in items" :key="index" :class="{ 'row-start': index % 2 === 0 }">
            {{ item }}
        </li>
    </ul>
</template>

<script>
export default {
    data() {
        return {
            items: ['项目1', '项目2', '项目3', '项目4', '项目5', '项目6']
        };
    }
};
</script>

<style>
.row-start {
    flex: 1 1 100% 50%; /* 每个项目占据50%的宽度 */
    flex-wrap: wrap;
}
</style>