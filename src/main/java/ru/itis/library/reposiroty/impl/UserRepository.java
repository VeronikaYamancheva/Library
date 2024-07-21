package ru.itis.library.reposiroty.impl;

import ru.itis.library.exceptions.DbException;
import ru.itis.library.model.User;
import ru.itis.library.model.User;
import ru.itis.library.reposiroty.CrudRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements CrudRepository<User, Integer> {
    private final static String fileName = "data/users.csv";
    private final List<User> data;
    private int autoincrementValue = 0;

    public UserRepository() throws DbException {
        data = readFile();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() > autoincrementValue) {
                autoincrementValue = data.get(i).getId();
            }
        }
    }
    
    @Override
    public User save(User item) throws DbException {
        if(item.getId() == null) {
            item.setId(++autoincrementValue);
            data.add(item);
            saveToFile();
            return item;
        }
        for(int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(item.getId())) {
                data.set(i, item);
                saveToFile();
                return item;
            }
        }
        throw new DbException("User with id " + item.getId() + " not found");
    }

    @Override
    public Optional<User> findById(Integer id) throws DbException {
        for(User user: data) {
            if (user.getId().equals(id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() throws DbException {
        return data;
    }

    @Override
    public void deleteById(Integer id) throws DbException {
        for(int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(id)) {
                data.remove(i);
                saveToFile();
                return;
            }
        }
    }
    
    private List<User> readFile() throws DbException {
        List<User> result = new ArrayList<>();
        try {
            InputStream stream = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                User user = new User(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]));
                result.add(user);
            }
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DbException("Data corrupted");
        }
        return result;
    }

    private void saveToFile() throws DbException {
        try {
            OutputStream stream = new FileOutputStream(fileName);
            PrintStream printStream = new PrintStream(stream, false, StandardCharsets.UTF_8);
            printStream.println("ID;NAME;AGE");
            for (User user : data) {
                printStream.printf("%d;%s;%d\n", user.getId(), user.getName(), user.getAge());
            }
            printStream.close();
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DbException("Data corrupted");
        }
    }

    public List<User> getData() {
        return data;
    }
}
