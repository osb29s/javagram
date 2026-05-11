# javagram

[![Build](https://github.com/osb29s/javagram/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/osb29s/javagram/actions/workflows/maven-publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.osb29s/javagram)](https://central.sonatype.com/artifact/io.github.osb29s/javagram)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

Spring Boot library for building Telegram bots with zero boilerplate. Annotate a class, implement one method — routing, matching, and priority are handled for you.

```java
@Command("/start")
@Component
public class StartAction implements Action {
    @Override
    public void run(Update update) {
        // handle /start
    }
}
```

---

## Installation

**Maven:**
```xml
<dependency>
    <groupId>io.github.osb29s</groupId>
    <artifactId>javagram</artifactId>
    <version>0.1.2-beta-1</version>
</dependency>
```

**Gradle:**
```groovy
implementation 'io.github.osb29s:javagram:0.1.2-beta-1'
```

---

## Configuration

Add these to `application.properties`:

| Property | Required | Description |
|---|---|---|
| `bot.token` | Yes | Token from [@BotFather](https://t.me/BotFather) |
| `bot.username` | Yes | Bot username, including the `@` symbol (e.g. `@mybot`) |
| `bot.path` | Webhook only | Full webhook URL (e.g. ngrok URL). Omit to use long polling. |

**Long polling** (local dev, no public URL needed):
```properties
bot.token=123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11
bot.username=@mybot
```

**Webhook** (production):
```properties
bot.token=123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11
bot.username=@mybot
bot.path=https://your-domain.com/webhook
```

---

## Quick start example

A minimal bot that responds to `/start`, greets users who say "hello", handles a delete button, and catches everything else.

**`application.properties`**
```properties
bot.token=YOUR_TOKEN_HERE
bot.username=@yourbotname
```

**`StartAction.java`** — responds to the `/start` command
```java
import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.annotations.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Command("/start")
@Component
public class StartAction implements Action {
    @Override
    public void run(Update update) {
        long chatId = update.getMessage().getChatId();
        // send a welcome message using the bot sender bean
    }
}
```

**`HelloAction.java`** — responds to any text containing "hello"
```java
import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.annotations.Text;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Text(contains = "hello")
@Component
public class HelloAction implements Action {
    @Override
    public void run(Update update) {
        // respond with a greeting
    }
}
```

**`DeleteAction.java`** — handles a callback from an inline button with key `"delete"`
```java
import com.rainett.javagram.action.Action;
import com.rainett.javagram.action.annotations.Callback;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Callback("delete")
@Component
public class DeleteAction implements Action {
    @Override
    public void run(Update update) {
        String data = update.getCallbackQuery().getData(); // e.g. "delete:42"
        // parse and delete item
    }
}
```

**`FallbackAction.java`** — called when no other action matches
```java
import com.rainett.javagram.update.service.impl.DefaultAction;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class FallbackAction implements DefaultAction {
    @Override
    public void run(Update update) {
        // reply with "I don't understand that"
    }
}
```

---

## Annotation reference

### `@Command`

Matches Telegram bot commands (messages starting with `/`).

```java
@Command("/help")           // matches exactly /help (and /help@botname)
@Command               // empty value = matches all commands
```

| Field | Type | Default | Description |
|---|---|---|---|
| `value` | `String` | `""` | Command to match. Must start with `/`. Empty matches all commands. |

`@botname` suffixes (e.g. `/start@mybot`) are stripped automatically.

---

### `@Text`

Matches plain text messages. All fields default to `""` (ignored when empty).

```java
@Text(equals = "hello")              // exact match
@Text(contains = "world")            // substring match
@Text(startsWith = "Dear")           // prefix match
@Text(endsWith = "thanks")           // suffix match
@Text(regex = "order #\\d+")         // Java regex match
```

| Field | Type | Default | Description |
|---|---|---|---|
| `equals` | `String` | `""` | Exact text match |
| `contains` | `String` | `""` | Text must contain this substring |
| `startsWith` | `String` | `""` | Text must start with this prefix |
| `endsWith` | `String` | `""` | Text must end with this suffix |
| `regex` | `String` | `""` | Text must match this Java regex |

**Priority order** (when multiple `@Text` actions could match):
`equals` > `regex` > `startsWith` / `endsWith` > `contains`

Within the same tier, longer pattern wins.

---

### `@Callback`

Matches inline keyboard callbacks. Callback data uses `:` as a delimiter — `value` matches the part before the first `:`.

```java
@Callback("delete")    // matches "delete" and "delete:42", "delete:some-id", etc.
@Callback              // empty value = matches all callbacks
```

| Field | Type | Default | Description |
|---|---|---|---|
| `value` | `String` | `""` | Callback key to match. Empty matches all callbacks. |

To attach data to a button and read it in the action:
```java
// building the button (key="delete", data="42"):
new InlineKeyboardBuilder()
    .row()
        .button("Delete item", "delete", "42")
    .endRow()
    .build();

// reading in DeleteAction:
String raw = update.getCallbackQuery().getData(); // "delete:42"
String itemId = raw.split(":")[1];               // "42"
```

---

## Action priority

Within each annotation type, javagram sorts actions by specificity so the most specific match wins — you never need to worry about ordering.

- **`@Command` / `@Callback`**: longer value = higher priority; empty value (catch-all) sorts last.
- **`@Text`**: `equals` > `regex` > `startsWith`/`endsWith` > `contains`; within the same tier, longer pattern wins.

---

## Sending inline keyboards

Use `InlineKeyboardBuilder` to construct keyboards fluently:

```java
import com.rainett.javagram.keyboard.InlineKeyboardBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
    .row()
        .button("Confirm", "confirm")           // callback_data = "confirm"
        .button("Cancel",  "cancel")            // callback_data = "cancel"
    .endRow()
    .row()
        .button("Delete item 42", "delete", "42") // callback_data = "delete:42"
    .endRow()
    .build();
```

- `button(text, key)` — `callback_data` = `key`
- `button(text, key, data)` — `callback_data` = `key:data`

Match the key in an action with `@Callback("confirm")`, `@Callback("delete")`, etc.

---

## Default action (fallback)

Implement `DefaultAction` and register it as a Spring bean. It is called whenever no annotated action matches the incoming update:

```java
@Component
public class FallbackAction implements DefaultAction {
    @Override
    public void run(Update update) {
        // handle unmatched updates
    }
}
```

If no `DefaultAction` bean exists, unmatched updates are silently ignored.

---

## Extending: adding a custom update type

To support a Telegram update type not covered by the built-in plugins (e.g. poll answers, inline queries), implement `UpdateTypePlugin<YourAnnotation>` and register it as a Spring component.

You need three things:

1. **A marker annotation** — annotated with `@BotAction` so classes using it are auto-discovered
2. **`UpdateTypeResolver`** — returns `true` when an `Update` belongs to your type
3. **`AbstractActionUpdateMatcher`** subclass — returns `true` when a specific action's annotation matches the update
4. **`AnnotationComparator`** — defines sort order (priority) among actions of your type

```java
@Component
public class PollAnswerPlugin implements UpdateTypePlugin<PollAnswer> {

    @Override
    public Class<PollAnswer> getAnnotationType() {
        return PollAnswer.class;
    }

    @Override
    @Bean
    public UpdateTypeResolver<PollAnswer> getUpdateTypeMatcher() {
        return new PollAnswerResolver();
    }

    @Override
    @Bean
    public ActionUpdateMatcher<PollAnswer> getActionMatcher() {
        return new PollAnswerMatcher();
    }

    @Override
    @Bean
    public AnnotationComparator<PollAnswer> getAnnotationComparator() {
        return new PollAnswerComparator();
    }
}
```

See `CommandPlugin`, `TextPlugin`, and `CallbackPlugin` in the source for complete examples.

---

## License

Apache 2.0 — see [LICENSE](LICENSE).
