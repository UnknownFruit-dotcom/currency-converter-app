import { myAxios } from "./axios";

export const conversionService = {
    convert: async (fromId, toId, amount) => {
        try {
            const response = await myAxios.get("/api/convert", {
                params: {
                    from: fromId,
                    to: toId,
                    amount,
                },
            });
            return response.data;
        } catch (error) {
            console.error("Error converting:", error);
            throw error;
        }
    },
};