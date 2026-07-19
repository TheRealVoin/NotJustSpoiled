package net.therealvoin.notjustspoiled.common.config;

import net.therealvoin.notjustspoiled.common.foodspoilage.FoodStatus;

public enum FoodCraftingMode {
    AVERAGE {
        @Override
        public boolean isAllowed(FoodStatus first, FoodStatus second) {
            return true;
        }

        @Override
        public CalculationMode getCalculationMode(boolean hasMixedStatuses) {
            return CalculationMode.AVERAGE;
        }
    },
    SAME_STATUS {
        @Override
        public boolean isAllowed(FoodStatus first, FoodStatus second) {
            return first == second;
        }

        @Override
        public CalculationMode getCalculationMode(boolean hasMixedStatuses) {
            return CalculationMode.AVERAGE;
        }
    },
    WORST_STATUS {
        @Override
        public boolean isAllowed(FoodStatus first, FoodStatus second) {
            return true;
        }

        @Override
        public CalculationMode getCalculationMode(boolean hasMixedStatuses) {
            return CalculationMode.WORST;
        }
    },
    FRESH_STATUS {
        @Override
        public boolean isAllowed(FoodStatus first, FoodStatus second) {
            return first == FoodStatus.FRESH && second == FoodStatus.FRESH;
        }

        @Override
        public CalculationMode getCalculationMode(boolean hasMixedStatuses) {
            return CalculationMode.AVERAGE;
        }
    },
    FRESH_OR_STALE_STATUS {
        @Override
        public boolean isAllowed(FoodStatus first, FoodStatus second) {
            return (first == FoodStatus.FRESH
                    || first == FoodStatus.STALE)
                    && (second == FoodStatus.FRESH
                    || second == FoodStatus.STALE);
        }

        @Override
        public CalculationMode getCalculationMode(boolean hasMixedStatuses) {
            return hasMixedStatuses ? CalculationMode.WORST : CalculationMode.AVERAGE;
        }
    };

    public abstract boolean isAllowed(FoodStatus first, FoodStatus second);

    public abstract CalculationMode getCalculationMode(boolean hasMixedStatuses);

    public enum CalculationMode {
        AVERAGE,
        WORST
    }
}