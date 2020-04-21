package com.sabir.training.rcp.demo.jfacebinding;


import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.sabir.training.rcp.demo.jfacebinding.model.Address;
import com.sabir.training.rcp.demo.jfacebinding.model.Person;


public class View extends ViewPart {
  public static final String ID = "com.sabir.training.rcp.demo.jfacebinding.view"; //$NON-NLS-1$
  private Person person;

  private Text firstName;
  private Text ageText;
  private Button marriedButton;
  private Combo genderCombo;
  private Text countryText;

  @Override
  public void createPartControl(Composite parent) {

    person = createPerson();
    // Lets put thing to order
    GridLayout layout = new GridLayout(2, false);
    layout.marginRight = 5;
    parent.setLayout(layout);

    Label firstLabel = new Label(parent, SWT.NONE);
    firstLabel.setText(Messages.View_1);
    firstName = new Text(parent, SWT.BORDER);

    GridData gridData = new GridData();
    gridData.horizontalAlignment = SWT.FILL;
    gridData.grabExcessHorizontalSpace = true;
    firstName.setLayoutData(gridData);

    Label ageLabel = new Label(parent, SWT.NONE);
    ageLabel.setText(Messages.View_2);
    ageText = new Text(parent, SWT.BORDER);

    gridData = new GridData();
    gridData.horizontalAlignment = SWT.FILL;
    gridData.grabExcessHorizontalSpace = true;
    ageText.setLayoutData(gridData);

    Label marriedLabel = new Label(parent, SWT.NONE);
    marriedLabel.setText(Messages.View_3);
    marriedButton = new Button(parent, SWT.CHECK);

    Label genderLabel = new Label(parent, SWT.NONE);
    genderLabel.setText(Messages.View_4);
    genderCombo = new Combo(parent, SWT.NONE);
    genderCombo.add(Messages.View_5);
    genderCombo.add(Messages.View_6);

    Label countryLabel = new Label(parent, SWT.NONE);
    countryLabel.setText(Messages.View_7);
    countryText = new Text(parent, SWT.BORDER);

    Button button1 = new Button(parent, SWT.PUSH);
    button1.setText("Write model"); //$NON-NLS-1$
    button1.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        System.out.println("Firstname" + person.getFirstName()); //$NON-NLS-1$
        System.out.println("Age" + person.getAge()); //$NON-NLS-1$
        System.out.println("Married" + person.isMarried()); //$NON-NLS-1$
        System.out.println("Gender" + person.getGender()); //$NON-NLS-1$
        System.out.println("Country" //$NON-NLS-1$
            + person.getAddress().getCountry());
      }
    });

    Button button2 = new Button(parent, SWT.PUSH);
    button2.setText(Messages.View_0);
    button2.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        person.setFirstName("Sabir"); //$NON-NLS-1$
        person.setAge(person.getAge() + 1);
        person.setMarried(!person.isMarried());
        if (person.getGender().equals("Male")) { //$NON-NLS-1$
            person.setGender("Male"); //$NON-NLS-1$
        } else {
          person.setGender("Female"); //$NON-NLS-1$
        }
        if (person.getAddress().getCountry().equals("Germany")) { //$NON-NLS-1$
          person.getAddress().setCountry("India"); //$NON-NLS-1$
        } else {
          person.getAddress().setCountry("Germany"); //$NON-NLS-1$
        }
      }
    });

    // now lets do the binding
    bindValues();
  }

  private Person createPerson() {
    Person person = new Person();
    Address address = new Address();
    address.setCountry("Deutschland"); //$NON-NLS-1$
    person.setAddress(address);
    person.setFirstName("John"); //$NON-NLS-1$
    person.setLastName("Doo"); //$NON-NLS-1$
    person.setGender("Male"); //$NON-NLS-1$
    person.setAge(12);
    person.setMarried(true);
    return person;
  }

  @Override
  public void setFocus() {
  }

  private void bindValues() {
    // The DataBindingContext object will manage the databindings
    // Lets bind it
    DataBindingContext ctx = new DataBindingContext();
    IObservableValue widgetValue = WidgetProperties.text(SWT.Modify)
        .observe(firstName);
    IObservableValue modelValue = BeanProperties.value(Person.class,
        "firstName").observe(person); //$NON-NLS-1$
    ctx.bindValue(widgetValue, modelValue);

    // Bind the age including a validator
    widgetValue = WidgetProperties.text(SWT.Modify).observe(ageText);
    modelValue = BeanProperties.value(Person.class, "age").observe(person); //$NON-NLS-1$
    // add an validator so that age can only be a number
    IValidator validator = new IValidator() {
      @Override
      public IStatus validate(Object value) {
        if (value instanceof Integer) {
          String s = String.valueOf(value);
          if (s.matches("\\d*")) { //$NON-NLS-1$
            return ValidationStatus.ok();
          }
        }
        return ValidationStatus.error("Not a number"); //$NON-NLS-1$
      }
    };

    UpdateValueStrategy strategy = new UpdateValueStrategy();
    strategy.setBeforeSetValidator(validator);

    Binding bindValue = ctx.bindValue(widgetValue, modelValue, strategy,
        null);
    // add some decorations
    ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.RIGHT);

    widgetValue = WidgetProperties.selection().observe(marriedButton);
    modelValue = BeanProperties.value(Person.class, "married").observe(person); //$NON-NLS-1$
    ctx.bindValue(widgetValue, modelValue);

    widgetValue = WidgetProperties.selection().observe(genderCombo);
    modelValue = BeanProperties.value("gender").observe(person); //$NON-NLS-1$

    ctx.bindValue(widgetValue, modelValue);

    // address field is bound to the Ui
    widgetValue = WidgetProperties.text(SWT.Modify).observe(countryText);

    modelValue = BeanProperties.value(Person.class, "address.country") //$NON-NLS-1$
        .observe(person);
    ctx.bindValue(widgetValue, modelValue);

  }
}