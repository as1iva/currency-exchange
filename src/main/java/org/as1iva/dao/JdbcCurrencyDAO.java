package org.as1iva.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.as1iva.exception.DatabaseException;
import org.as1iva.model.Currency;
import org.as1iva.util.ConnectionManager;

public class JdbcCurrencyDAO implements CurrencyDAO {

    private static final JdbcCurrencyDAO INSTANCE = new JdbcCurrencyDAO();

    private static final String ADD_SQL = """
            INSERT INTO Currencies(Code, FullName, Sign)
            VALUES (?, ?, ?)
            """;

    private static final String GET_BY_CODE_SQL = """
            SELECT ID, Code, FullName, Sign
            FROM Currencies
            WHERE Code = ?
            """;

    private static final String GET_ALL_SQL = """
            SELECT ID, Code, FullName, Sign
            FROM Currencies
            """;

    private static final String UPDATE_SQL = """
            UPDATE Currencies
            SET Code = ?,
                FullName = ?,
                Sign = ?
            WHERE ID = ?
            """;

    private static final int ID_COLUMN_INDEX = 1;

    private JdbcCurrencyDAO() {
    }

    public static JdbcCurrencyDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public Currency add(Currency currency) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                currency.setId(generatedKeys.getInt(ID_COLUMN_INDEX));
            }

            return currency;
        } catch(SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
    }

    @Override
    public Optional<Currency> getByCode(String code) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_CODE_SQL)) {
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            Currency currency = null;

            while (resultSet.next()) {
                currency = new Currency(
                        resultSet.getInt("ID"),
                        resultSet.getString("Code"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Sign")
                );
            }
            return Optional.ofNullable(currency);
        } catch(SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
    }

    @Override
    public List<Currency> getAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Currency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                currencies.add(new Currency(
                        resultSet.getInt("ID"),
                        resultSet.getString("Code"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Sign")
                ));
            }
            return currencies;
        } catch(SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
    }

    @Override
    public void update(Currency currency) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.setInt(4, currency.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Database is unavailable");
        }
    }
}
