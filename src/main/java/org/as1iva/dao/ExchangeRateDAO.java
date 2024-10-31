package org.as1iva.dao;

import org.as1iva.models.Currency;
import org.as1iva.models.ExchangeRate;
import org.as1iva.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAO implements ExchangeRateDAOInterface<ExchangeRate>{

    private static final ExchangeRateDAO INSTANCE = new ExchangeRateDAO();

    private static final String ADD_SQL = """
            INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
            VALUES (?, ?, ?)
            """;

    private static final String GET_BY_CODE_SQL = """
            SELECT
                ER.ID AS ExchangeRateId,
                BC.ID AS BaseCurrencyId,
                BC.Code AS BaseCurrencyCode,
                BC.FullName AS BaseCurrencyName,
                BC.Sign AS BaseCurrencySign,
                TC.ID AS TargetCurrencyId,
                TC.Code AS TargetCurrencyCode,
                TC.FullName AS TargetCurrencyName,
                TC.Sign AS TargetCurrencySign,
                ER.Rate
            
            FROM ExchangeRates ER
            LEFT JOIN main.Currencies BC on ER.BaseCurrencyId = BC.ID
            LEFT JOIN main.Currencies TC on ER.TargetCurrencyId = TC.ID
            WHERE BaseCurrencyCode = ? AND TargetCurrencyCode = ?
            """;

    private static final String GET_ALL_SQL = """
            SELECT
                ER.ID AS ExchangeRateId,
                BC.ID AS BaseCurrencyId,
                BC.Code AS BaseCurrencyCode,
                BC.FullName AS BaseCurrencyName,
                BC.Sign AS BaseCurrencySign,
                TC.ID AS TargetCurrencyId,
                TC.Code AS TargetCurrencyCode,
                TC.FullName AS TargetCurrencyName,
                TC.Sign AS TargetCurrencySign,
                ER.Rate
            
            FROM ExchangeRates ER
            LEFT JOIN main.Currencies BC on ER.BaseCurrencyId = BC.ID
            LEFT JOIN main.Currencies TC on ER.TargetCurrencyId = TC.ID
            """;

    private static final String UPDATE_SQL = """
            UPDATE ExchangeRates
            SET Rate = ?
            WHERE BaseCurrencyId = (SELECT id FROM Currencies WHERE Code = ?)
            AND TargetCurrencyId = (SELECT id FROM Currencies WHERE Code = ?)
            """;

    private static final int ID_COLUMN_INDEX = 1;

    private ExchangeRateDAO() {
    }

    public static ExchangeRateDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRate add(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL)) {

            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                exchangeRate.setId(generatedKeys.getInt(ID_COLUMN_INDEX));
            }

            return exchangeRate;
        }
    }

    @Override
    public Optional<ExchangeRate> getByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_CODE_SQL)) {

            preparedStatement.setObject(1, baseCurrencyCode);
            preparedStatement.setObject(2, targetCurrencyCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            ExchangeRate exchangeRate = null;

            while (resultSet.next()) {
                exchangeRate = new ExchangeRate(
                        resultSet.getInt("ExchangeRateId"),
                        new Currency(
                                resultSet.getInt("BaseCurrencyId"),
                                resultSet.getString("BaseCurrencyCode"),
                                resultSet.getString("BaseCurrencyName"),
                                resultSet.getString("BaseCurrencySign")
                        ),
                        new Currency(
                                resultSet.getInt("TargetCurrencyId"),
                                resultSet.getString("TargetCurrencyCode"),
                                resultSet.getString("TargetCurrencyName"),
                                resultSet.getString("TargetCurrencySign")
                        ),
                        resultSet.getBigDecimal("Rate")
                );
            }
            return Optional.ofNullable(exchangeRate);
        }
    }

    @Override
    public List<ExchangeRate> getAll() throws SQLException {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<ExchangeRate> exchangeRates = new ArrayList<>();

            while (resultSet.next()) {
                exchangeRates.add(new ExchangeRate(
                        resultSet.getInt("ExchangeRateId"),
                        new Currency(
                                resultSet.getInt("BaseCurrencyId"),
                                resultSet.getString("BaseCurrencyCode"),
                                resultSet.getString("BaseCurrencyName"),
                                resultSet.getString("BaseCurrencySign")
                        ),
                        new Currency(
                                resultSet.getInt("TargetCurrencyId"),
                                resultSet.getString("TargetCurrencyCode"),
                                resultSet.getString("TargetCurrencyName"),
                                resultSet.getString("TargetCurrencySign")
                        ),
                        resultSet.getBigDecimal("Rate")
                ));
            }
            return exchangeRates;
        }
    }

    @Override
    public void update(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setBigDecimal(1, exchangeRate.getRate());
            preparedStatement.setString(2, exchangeRate.getBaseCurrencyCode());
            preparedStatement.setString(3, exchangeRate.getTargetCurrencyCode());

            preparedStatement.executeUpdate();
        }
    }
}
