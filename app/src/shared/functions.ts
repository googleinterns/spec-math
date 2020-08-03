import { ComponentFixture } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

export const queryElement = (targetFixture: ComponentFixture<any>, targetClass: string) => {
  return targetFixture.debugElement.query(By.css(targetClass));
};

export const readFileAsString = (file: File): Promise<string> => {
  const reader = new FileReader();
  return new Promise((resolve) => {
    reader.readAsText(file);
    reader.onload = () => {
      resolve(reader.result.toString());
    };
  });
};
