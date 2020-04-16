package test.aggregators;

import model.User;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

public class UserAggregator implements ArgumentsAggregator {

    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context)
            throws ArgumentsAggregationException {
        return new User(
                accessor.getString(0),
                accessor.getString(1),
                accessor.getString(2),
                accessor.getString(3),
                accessor.getString(4),
                null
        );
    }
}