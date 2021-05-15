package todo.repository;

import org.junit.Test;
import todo.model.Item;
import todo.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ItemRepositoryTest {

    private  ItemRepository repository = ItemRepository.getInstance();

    @Test
    public void save() {
        Item item  = Item.builder().description("new description").build();
        repository.save(item);
        assertThat(item.getId(), instanceOf(Integer.class));
        item.setDescription("Save Description");
        repository.save(item);
        Item find = repository.findById(Item.class, item.getId());
        assertThat(find.getId(), instanceOf(Integer.class));
        assertThat(find.getId(), is(item.getId()));
        assertThat(find.getDescription(), is("Save Description"));
    }

    @Test
    public void findAll() {
        repository.add(Item.builder().description("new 1 description").build());
        repository.add(Item.builder().description("new 2 description").build());
        repository.add(Item.builder().description("new 3 description").build());

        List<Item> items = repository.findAll(Item.class);
        assertThat(items.size(), is(7));
    }

    @Test
    public void whenAdd() {
        Item item = Item.builder().description("new add description").build();
        repository.add(item);
        assertThat(item.getId(), instanceOf(Integer.class));
        assertThat(item.getDescription(), is("new add description"));
    }

    @Test
    public void findById() {
        Item item  = Item.builder().description("new description").build();
        repository.add(item);
        Item search = repository.findById(Item.class,item.getId());
        assertThat(search.getId(), instanceOf(Integer.class));
        assertThat(search.getId(), is(item.getId()));
        assertThat(search.getDescription(), is("new description"));
    }

    @Test
    public void findByIdNotFound() {
        Item item  = Item.builder().description("new description").build();
        repository.add(item);
        Item search = repository.findById(Item.class, 100000);
        assertThat(search, nullValue());
    }

    @Test
    public void findByDoneAll() {
        repository.add(Item.builder().done(true).description("new 1 description").build());
        repository.add(Item.builder().done(false).description("new 2 description").build());
        repository.add(Item.builder().done(false).description("new 3 description").build());
        List<Item> items = repository.findByDoneAll(true);
        assertThat(items.size(), is(1));
    }

    @Test
    public void findUserByEmail() {
        User user1 = User.builder().email("admin@mail.ru").password("12334").build();
        User user2 = User.builder().email("user@mail.ru").password("4321").build();
        repository.add(user1);
        repository.add(user2);

        User search = repository.findByEmail("admin@mail.ru");
        assertThat(search.getEmail(), is("admin@mail.ru"));
        assertThat(search.getId(), instanceOf(Integer.class));
    }

    @Test
    public void findUserByEmailNotFound() {
        User search = repository.findByEmail("NotExist@email.ru");
        assertThat(search, nullValue());
    }

    @Test
    public void delete() {
        Item item1 = Item.builder().description("new 2 description").build();
        Item item2 = Item.builder().description("new 3 description").build();
        repository.add(Item.builder().description("new 1 description").build());
        repository.add(item1);
        repository.add(item2);

        repository.delete(item1);
        repository.delete(item2);

        List<Item> items = repository.findAll(Item.class);
        assertThat(items.size(), is(1));
    }

    @Test
    public void replace() {

        Item item  = Item.builder().description("new description").build();
        repository.add(item);
        item.setDescription("replaced Description");
        repository.replace(item);
        Item find = repository.findById(Item.class, item.getId());
        assertThat(find.getId(), instanceOf(Integer.class));
        assertThat(find.getId(), is(item.getId()));
        assertThat(find.getDescription(), is("replaced Description"));
    }
}