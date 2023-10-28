import { RouteParams, useRoute, useRouter } from "vue-router";
import { ComputedRef } from "vue";
import { computed } from "vue";

interface Navigation {
  navigateTo: (routeName: string, params?: Record<string, unknown>) => void;
  navigateBack: () => void;
  getCurrentRouteName: ComputedRef<string>;
  getCurrentParams: RouteParams
}

export function useNavigation(): Navigation {
  const router = useRouter();
  const route = useRoute();
  const navigateTo = (routeName: string, params = {}) => {
    router.push({ name: routeName, params });
  };
  const navigateBack = () => {
    router.go(-1);
  };

  const getCurrentRouteName = computed(() => {
    // console.log(route.name, "routeName");
    return route.name as string;
  });

  const getCurrentParams = route.params

  return {
    navigateTo,
    navigateBack,
    getCurrentRouteName,
    getCurrentParams,
  };
}
