# Contributing to Smart Disk Analyzer

First off, thank you for considering contributing to Smart Disk Analyzer! It's people like you that make this project a great tool for everyone. 🎉

## 🌟 Why Contribute?

Smart Disk Analyzer is a **free and open-source alternative** to expensive commercial disk management tools. By contributing, you're helping to:

- Provide a truly free tool to users worldwide
- Build a community-driven project
- Learn and improve your skills
- Make disk management accessible to everyone

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
  - [Reporting Bugs](#reporting-bugs)
  - [Suggesting Features](#suggesting-features)
  - [Code Contributions](#code-contributions)
  - [Documentation](#documentation)
  - [Testing](#testing)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Pull Request Process](#pull-request-process)
- [Community](#community)

## 📜 Code of Conduct

This project and everyone participating in it is governed by our commitment to:

- **Be respectful** and inclusive
- **Be collaborative** and helpful
- **Be patient** with others
- **Be constructive** in feedback
- **No harassment** or discrimination of any kind

## 🤝 How Can I Contribute?

### 🐛 Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates.

**Good bug reports** should include:

- **Clear title**: Descriptive one-line summary
- **Steps to reproduce**: Detailed steps to recreate the issue
- **Expected behavior**: What should happen
- **Actual behavior**: What actually happens
- **Environment**: OS version, Java version, app version
- **Screenshots**: If applicable
- **Logs**: Any error messages or stack traces

**Example:**

```markdown
### Bug: Partition resize fails on MBR disks

**Steps to Reproduce:**
1. Open Smart Disk Analyzer as administrator
2. Select a partition on an MBR disk
3. Click "Resize" and try to extend by 10 GB
4. Click "Apply"

**Expected:** Partition should resize successfully
**Actual:** Error message: "Diskpart failed with code 1"

**Environment:**
- Windows 11 Pro 23H2
- Smart Disk Analyzer v1.0.0
- Java 21.0.7

**Logs:**
```
Error executing diskpart: ...
```
```

### 💡 Suggesting Features

We love feature suggestions! Before submitting:

- Check if the feature is already in the [ROADMAP.md](ROADMAP.md)
- Search existing feature requests
- Consider if it aligns with the project goals

**Good feature requests** should include:

- **Clear use case**: Why is this needed?
- **Proposed solution**: How should it work?
- **Alternatives**: Other ways to solve the problem
- **AOMEI comparison**: Does AOMEI have this feature?

**Example:**

```markdown
### Feature Request: Disk Cloning

**Use Case:** 
Users upgrading to new disks need to clone their entire system

**Proposed Solution:**
Add a "Clone Disk" feature that:
1. Creates a sector-by-sector copy
2. Supports both MBR and GPT
3. Allows selecting source and destination
4. Shows progress with estimated time

**AOMEI Comparison:**
AOMEI Pro has this as a premium feature ($49.95)
We should offer it for free!
```

### 💻 Code Contributions

#### Areas We Need Help

**High Priority:**
- 🔴 **Boot Management**: Bootable USB creation, boot repair
- 🔴 **Disk Cloning**: Full disk imaging and cloning
- 🔴 **S.M.A.R.T. Monitoring**: Disk health monitoring
- 🟡 **UI Improvements**: Better error messages, progress indicators
- 🟡 **Testing**: Unit tests, integration tests

**Good First Issues:**
- Documentation improvements
- UI polish (icons, colors, layouts)
- Error message improvements
- Adding tooltips
- Translation support

### 📝 Documentation

Documentation is just as important as code!

**You can help by:**
- Improving README.md
- Writing user guides
- Creating video tutorials
- Adding code comments
- Writing API documentation
- Translating to other languages

### 🧪 Testing

Help us ensure quality by:

- Testing new features
- Testing on different Windows versions
- Testing with different disk configurations
- Writing automated tests
- Reporting bugs you find

## 🛠️ Development Setup

### Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.6+
- **Node.js**: 16+ with npm
- **Git**: Latest version
- **IDE**: IntelliJ IDEA, VS Code, or Eclipse

### Clone and Setup

```bash
# Fork the repository first on GitHub!

# Clone your fork
git clone https://github.com/YOUR-USERNAME/Smart-Disk-Analyser.git
cd Smart-Disk-Analyser

# Add upstream remote
git remote add upstream https://github.com/Dharaneesh20/Smart-Disk-Analyser.git

# Install backend dependencies
cd backend
mvn clean install

# Install frontend dependencies
cd ../frontend
npm install

# Return to root
cd ..
```

### Running Locally

```bash
# Option 1: Use the convenience script
start.bat

# Option 2: Run manually

# Terminal 1 - Backend
cd backend
mvn spring-boot:run

# Terminal 2 - Frontend  
cd frontend
npm start
```

Access at http://localhost:3000

### Testing Your Changes

```powershell
# Test backend
cd backend
mvn test

# Test frontend
cd frontend
npm test

# Test all endpoints
cd ..
.\test-api.ps1
```

### Building for Production

```powershell
# Build everything
.\build-windows.ps1
```

## 📐 Coding Standards

### Java (Backend)

```java
// ✅ Good
@Service
public class WindowsPartitionService {
    private static final Logger logger = LoggerFactory.getLogger(WindowsPartitionService.class);
    
    /**
     * Executes diskpart commands from a script file
     * @param commands List of diskpart commands
     * @return Command output
     * @throws IOException if execution fails
     */
    public String executeDiskpartScript(List<String> commands) throws IOException {
        // Implementation with proper error handling
    }
}

// ❌ Bad
public class service {
    public String doStuff(List<String> c) {
        // No docs, no error handling, bad naming
    }
}
```

**Java Guidelines:**
- Use **meaningful names** for variables and methods
- Add **Javadoc** for public methods
- Follow **Spring Boot best practices**
- Handle **exceptions properly**
- Add **logging** for important operations
- Use **@Service**, **@Controller**, **@Repository** annotations

### JavaScript/React (Frontend)

```javascript
// ✅ Good
const PartitionCard = ({ partition, onAction }) => {
    const [loading, setLoading] = useState(false);
    
    const handleResize = async () => {
        try {
            setLoading(true);
            await partitionService.resize(partition.id, newSize);
            showSuccess('Partition resized successfully!');
        } catch (error) {
            showError('Failed to resize partition');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };
    
    return (
        <Card>
            {/* Clean JSX with proper structure */}
        </Card>
    );
};

// ❌ Bad
const thing = (props) => {
    // No error handling, unclear logic
    return <div onClick={() => doThing()}>{props.stuff}</div>;
};
```

**React Guidelines:**
- Use **functional components** with hooks
- Implement **error handling** for API calls
- Add **loading states** for async operations
- Follow **Material-UI** patterns
- Use **semantic HTML**
- Keep components **small and focused**

### General Guidelines

- **Code Format**: 4 spaces for indentation
- **Line Length**: Max 120 characters
- **Comments**: Explain "why", not "what"
- **Variables**: camelCase for JS, camelCase for Java
- **Constants**: UPPER_SNAKE_CASE
- **Files**: One component per file
- **Commits**: Clear, descriptive messages

### Commit Messages

Use the [Conventional Commits](https://www.conventionalcommits.org/) format:

```
feat: add disk cloning feature
fix: resolve partition resize bug on GPT disks
docs: update installation instructions
test: add unit tests for WindowsPartitionService
refactor: improve error handling in DiskScannerService
style: format code according to style guide
chore: update dependencies
```

**Examples:**

```bash
# ✅ Good
git commit -m "feat: implement MBR to GPT conversion"
git commit -m "fix: handle null pointer in duplicate finder"
git commit -m "docs: add API documentation for partition endpoints"

# ❌ Bad
git commit -m "updates"
git commit -m "fixed bug"
git commit -m "changes"
```

## 🔄 Pull Request Process

### Before Submitting

1. **Update your fork**
   ```bash
   git checkout main
   git fetch upstream
   git merge upstream/main
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Make your changes**
   - Write clean, documented code
   - Add tests if applicable
   - Update documentation

4. **Test thoroughly**
   ```bash
   mvn test         # Backend tests
   npm test         # Frontend tests
   .\test-api.ps1   # API tests
   ```

5. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: add amazing feature"
   ```

6. **Push to your fork**
   ```bash
   git push origin feature/amazing-feature
   ```

### Submitting the PR

1. Go to your fork on GitHub
2. Click "New Pull Request"
3. Fill in the template:

```markdown
## Description
Brief description of what this PR does

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] All existing tests pass
- [ ] Added new tests for new features
- [ ] Tested manually on Windows 11

## Screenshots (if applicable)
[Add screenshots here]

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No new warnings
```

### Review Process

- **Maintainer review**: Usually within 2-3 days
- **Feedback**: Address any requested changes
- **Approval**: At least one maintainer approval needed
- **Merge**: Maintainer will merge when ready

### After Merge

```bash
# Update your local main branch
git checkout main
git pull upstream main
git push origin main

# Delete the feature branch
git branch -d feature/amazing-feature
git push origin --delete feature/amazing-feature
```

## 🏆 Recognition

Contributors will be:
- Listed in the [Contributors](https://github.com/Dharaneesh20/Smart-Disk-Analyser/graphs/contributors) page
- Mentioned in release notes
- Added to README.md acknowledgments

## 💬 Community

- **GitHub Discussions**: Ask questions, share ideas
- **Issues**: Report bugs and request features
- **Pull Requests**: Contribute code and documentation

## 📞 Getting Help

Stuck? Need guidance?

- Check existing documentation
- Search [Issues](https://github.com/Dharaneesh20/Smart-Disk-Analyser/issues)
- Ask in [Discussions](https://github.com/Dharaneesh20/Smart-Disk-Analyser/discussions)
- Read the [ROADMAP.md](ROADMAP.md) for project direction

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev/)
- [Material-UI Documentation](https://mui.com/)
- [Electron Documentation](https://www.electronjs.org/docs)
- [Windows Diskpart Commands](https://learn.microsoft.com/en-us/windows-server/administration/windows-commands/diskpart)

---

## 🙏 Thank You!

Every contribution, no matter how small, makes a difference. Whether it's fixing a typo, reporting a bug, or adding a major feature - **we appreciate you!**

Together, we're building the best free and open-source disk management tool. 🚀

---

<p align="center">
  <strong>Made with ❤️ by the Smart Disk Analyzer community</strong>
</p>

<p align="center">
  <strong>Free Forever | Open Source Always | Community Driven</strong>
</p>
