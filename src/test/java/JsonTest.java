import com.google.gson.Gson;
import org.junit.Test;

/**
 * Created in project RiddlesAndPuzzles in 24.12.2016
 */
public class JsonTest {
	@Test
	public void json01() {
		// создаю сложный объект
		Man Oleg = new Man("Oleg", "Putin", 65, null, null);
		Oleg.books = new String[]{"Pushkin", "Lermontov", "Tolstoy"};

		// создаю второй сложный объект
		Man Vasya = new Man("Vasya", "Putin", 33, Oleg, null);
		Vasya.boxes = new Object[]{"Варшава", 125, true, 1000f, .0001, 0xFFF, null,
			new String[]{"Kolya", "Petya"},
			new Integer[]{123, 456, 789},
			new Object()};

		// Жаль, но Gson не умеет разруливать перекрестные ссылки
		// Следующая строка вызовет StackOverflow
		// Oleg.son = Vasya;
		// За рекурсиями тоже стоит следить
		// Oleg.son = Oleg;

		System.out.println(Oleg);
		System.out.println(Vasya);
		System.out.println();

		Gson gson = new Gson();

		// convert to Json
		String sOleg = gson.toJson(Oleg);
		String sVasya = gson.toJson(Vasya);

		System.out.println(sOleg);
		System.out.println(sVasya);
		System.out.println();

		// convert from Json
		Man Oleg1 = gson.fromJson(sOleg, Man.class);
		Man Vasya1 = gson.fromJson(sVasya, Man.class);

		System.out.println(Oleg1);
		System.out.println(Vasya1);
	}

	class Man {
		String name, surname;
		int age;
		String[] books;
		Object[] boxes;
		Man father, son;

		public Man(String name, String surname, int age, Man father, Man son) {
			this.name = name;
			this.surname = surname;
			this.age = age;
			this.father = father;
			this.son = son;
		}

		@Override
		public String toString() {
			// из Object[] формирую String[]
			String[] b = null;
			if (boxes != null) {
				b = new String[boxes.length];
				for (int i = 0; i < boxes.length; i++) {
					b[i] = boxes[i] == null ? "null" : boxes[i].toString();
				}
			}

			return String.format("id = %d, Name = %s, Surname = %s, Age = %s, Father = %s, Son = %s, Books = %s, Boxes = %s",
					hashCode(), name, surname, age,
					father == null ? father : "\n{\n\t" + father + "\n}",
					son == null ? son : "\n{\n\t" + son + "\n}",
					books == null ? books : "\n{\n\t" + String.join(", ", books) + "\n}",
					boxes == null ? boxes : "\n{\n\t" + String.join(", ", b) + "\n}");
		}
	}

}
