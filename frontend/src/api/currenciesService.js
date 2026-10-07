import { myAxios } from "./axios";

export const currenciesService = {
    getAllCurrencies: async () => {
        try {
            const response = await myAxios.get("/api/currencies");
            return response.data;
        } catch (error) {
            console.error("Error fetching currencies:", error);
            throw error;
        }
    },
};