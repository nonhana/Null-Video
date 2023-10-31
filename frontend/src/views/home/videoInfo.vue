<template>
    <div class="video-info">
        <div class="author">
            <div class="author-header">
                <img src="" alt="" />
            </div>
            <div class="author-info">
                <div class="author-name">{{ 'TZX' }}</div>
                <div class="author-fence">{{ 100 }} 位粉丝</div>
            </div>
            <Button height="2.25rem" width="4.5rem">关注</Button>
        </div>

        <div class="video-intro">
            {{ '谁懂啊，路边遇到🦞头👨想加我vx……' }}
        </div>

        <div class="video-tags">
            <div v-for="tag in tags" :key="tag.id" :style="{ background: tag.color }">
                {{ tag.name }}
            </div>
        </div>

        <div class="video-operation">
            <div>
                <img src="@/assets/svgs/like.svg" alt="" />
                <div>
                    {{ 1 }}
                </div>
            </div>
            <div>
                <img src="@/assets/svgs/collection.svg" alt="" />
                <div>
                    {{ 1 }}
                </div>
            </div>
            <div>
                <img src="@/assets/svgs/share.svg" alt="" />
                <div>
                    {{ 1 }}
                </div>
            </div>
            <div>
                <img src="@/assets/svgs/comment.svg" alt="" />
                <div>
                    {{ 1 }}
                </div>
            </div>




        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import Button from '@nullVideo/button/button.vue'

const tags: { name: string; id: string; color: string }[] = reactive([{
    name: '吐槽',
    id: '123123',
    color: '#ff8200'
}, {
    name: '无语死了',
    id: '123123123',
    color: '#4a91ee'
}])


</script>
<style scoped lang="less">
.video-info {
    width: 100%;

    .author {
        display: flex;

        .author-header {
            width: 3rem;
            height: 3rem;
            border-radius: 100%;
            background-color: rgb(8, 123, 255);
        }

        .author-info {
            margin-left: 0.5rem;
            margin-right: 2rem;
            font-size: 0.875rem;

            .author-name {
                color: @text;
            }

            .author-fence {
                color: @text-secondary;
            }
        }
    }

    .video-tags {
        display: flex;
        justify-content: start;
        margin-bottom: 1rem;

        div {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 2rem;
            padding: 1rem;
            border-radius: @border-radius;
            box-shadow: @shadow-outer;
            color: #fff;
            font-size: 0.875rem;
            font-weight: bold;
            margin-right: 1rem;
        }
    }

    .video-intro {
        margin: 1rem 0;
        width: 100%;
        padding: 1rem;
        background-color: #fff;
        font-size: 1rem;
        border: 1px solid #d4d4d4;
        border-radius: @border-radius;
        color: @text;
    }

    .video-operation {
        display: flex;
        justify-content: space-around;

        >div {
            cursor: pointer;

            >div {
                margin-top: -0.75rem;
                text-align: center;
                font-size: 1rem;
                color: @text-secondary;
            }
        }
    }
}
</style>
