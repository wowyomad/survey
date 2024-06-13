const languages = {
    en: {
        placeholder: "Enter your name",
        greet: "Greet",
        message: "Hello, "
    },
    ru: {
        placeholder: "Введите ваше имя",
        greet: "Приветствовать",
        message: "Здравствуйте, "
    }
};

document.getElementById('languageDropdown').addEventListener('change', function() {
    const selectedLanguage = this.value;
    document.getElementById('nameInput').placeholder = languages[selectedLanguage].placeholder;
    document.getElementById('greetButton').textContent = languages[selectedLanguage].greet;
});

document.getElementById('greetButton').addEventListener('click', function() {
    const name = document.getElementById('nameInput').value;
    const messageBox = document.getElementById('messageBox');
    const overlay = document.getElementById('overlay');
    const selectedLanguage = document.getElementById('languageDropdown').value;

    if (name) {
        messageBox.textContent = `${languages[selectedLanguage].message}${name}`;
        messageBox.style.display = 'block';
        overlay.style.display = 'block';
    }
});

document.getElementById('overlay').addEventListener('click', function() {
    const messageBox = document.getElementById('messageBox');
    const overlay = document.getElementById('overlay');

    messageBox.style.display = 'none';
    overlay.style.display = 'none';
});
